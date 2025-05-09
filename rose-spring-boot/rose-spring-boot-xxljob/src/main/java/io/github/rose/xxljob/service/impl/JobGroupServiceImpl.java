/*
 * Copyright © 2025 rose-group.github.io
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
package io.github.rose.xxljob.service.impl;

import io.github.rose.core.exception.BusinessException;
import io.github.rose.xxljob.model.XxlJobGroup;
import io.github.rose.xxljob.model.XxlJobGroupPage;
import io.github.rose.xxljob.model.XxlRestResponse;
import io.github.rose.xxljob.service.JobGroupService;
import io.github.rose.xxljob.service.JobLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class JobGroupServiceImpl implements JobGroupService {

    private final JobLoginService jobLoginService;

    private final RestTemplate restTemplate;

    private final String host;

    @Override
    public List<XxlJobGroup> getJobGroup(String appName) {
        String url = host + "/jobgroup/pageList";

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("appname", appName);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.set("Cookie", jobLoginService.getCookie());

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(map, headers);

        ResponseEntity<XxlJobGroupPage> response =
            restTemplate.postForEntity(url, requestEntity, XxlJobGroupPage.class);
        List<XxlJobGroup> jobGroup = response.getBody().getData();

        return jobGroup.stream()
            .filter(xxlJobGroup -> xxlJobGroup.getAppname().equals(appName))
            .collect(Collectors.toList());
    }

    @Override
    public void autoRegisterGroup(String appName, String title) {
        String url = host + "/jobgroup/save";

        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("appname", appName);
        map.add("title", appName);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Cookie", jobLoginService.getCookie());

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(map, headers);

        ResponseEntity<XxlRestResponse> response =
            restTemplate.postForEntity(url, requestEntity, XxlRestResponse.class);

        XxlRestResponse xxlRestResponse = response.getBody();
        if (xxlRestResponse.getCode() != 200) {
            throw new BusinessException(xxlRestResponse.getMsg());
        }
    }
}
