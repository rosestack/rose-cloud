/*
 * Copyright © 2025 rosestack.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.rose.redis.zk;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.ReentrantLock;

public class ZkLockImpl implements ZkLock, InitializingBean {

    private static final String LOCK_ROOT_PATH = "/ZkLock";
    private static final Logger log = LoggerFactory.getLogger(ZkLockImpl.class);
    private final CuratorFramework curatorFramework;
    private final Map<String, CountDownLatch> concurrentMap = new ConcurrentHashMap<>();
    private final ReentrantLock lock = new ReentrantLock();

    public ZkLockImpl(CuratorFramework curatorFramework) {
        this.curatorFramework = curatorFramework;
    }

    @Override
    public boolean lock(String lockpath) {
        boolean result = false;
        String keyPath = LOCK_ROOT_PATH + lockpath;
        try {
            curatorFramework
                .create()
                .creatingParentsIfNeeded()
                .withMode(CreateMode.EPHEMERAL)
                .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                .forPath(keyPath);
            result = true;
            log.info("success to acquire mutex lock for path:{}", keyPath);
        } catch (Exception e) {
            log.info(
                "Thread:{};failed to acquire mutex lock for path:{}",
                Thread.currentThread().getName(),
                keyPath);
            if (!concurrentMap.containsKey(lockpath)) {
                try {
                    /*
                     * 这里考虑到高并发场景,必须保证对同一个节点加锁的线程失败后是落在同一个countdown对象上 ,否则有的线程永远没有办法唤醒了
                     */
                    lock.lock();
                    // 双重校验,考虑高并发问题
                    if (!concurrentMap.containsKey(lockpath)) {
                        concurrentMap.put(lockpath, new CountDownLatch(1));
                    }
                } finally {
                    lock.unlock();
                }
            }
            try {
                CountDownLatch countDownLatch = concurrentMap.get(lockpath);
                // 这里为什么要判断呢？大家可以思考一下,高并发场景
                if (countDownLatch != null) {
                    countDownLatch.await();
                }
            } catch (InterruptedException e1) {
                log.info("InterruptedException message:{}", e1.getMessage());
            }
        }
        return result;
    }

    @Override
    public boolean unlock(String lockpath) {
        String keyPath = LOCK_ROOT_PATH + lockpath;
        try {
            if (curatorFramework.checkExists().forPath(keyPath) != null) {
                curatorFramework.delete().forPath(keyPath);
            }
        } catch (Exception e) {
            log.error("failed to release mutex lock");
            return false;
        }
        return true;
    }

    /**
     * 监听节点事件
     *
     * @param lockPath 加锁的路径
     */
    private void addWatcher(String lockPath) throws Exception {
        String keyPath;
        if (LOCK_ROOT_PATH.equals(lockPath)) {
            keyPath = lockPath;
        } else {
            keyPath = LOCK_ROOT_PATH + lockPath;
        }

        final PathChildrenCache cache = new PathChildrenCache(curatorFramework, keyPath, false);
        cache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);

        /*
         * 添加监听器
         */
        cache.getListenable().addListener((client, event) -> {
            if (event.getType().equals(PathChildrenCacheEvent.Type.CHILD_REMOVED)) {
                String oldPath = event.getData().getPath();
                log.info("oldPath delete:{},redis缓存已经更新！", oldPath);
                if (oldPath.contains(lockPath)) {
                    CountDownLatch countDownLatch = concurrentMap.remove(oldPath);
                    if (countDownLatch != null) { // 有可能没有竞争,countdown不存在
                        countDownLatch.countDown();
                    }
                }
            }
        });
    }

    @Override
    public void afterPropertiesSet() {
        curatorFramework.usingNamespace("zklock-namespace");
        try {
            if (curatorFramework.checkExists().forPath(LOCK_ROOT_PATH) == null) {
                curatorFramework
                    .create()
                    .creatingParentsIfNeeded()
                    .withMode(CreateMode.PERSISTENT)
                    .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                    .forPath(LOCK_ROOT_PATH);
            }
            // 启动监听器
            addWatcher(LOCK_ROOT_PATH);
        } catch (Exception e) {
            log.error("connect zookeeper failed:{}", e.getMessage(), e);
        }
    }
}
