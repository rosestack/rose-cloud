ARG TARGETPLATFORM
FROM --platform=${TARGETPLATFORM:-linux/amd64} mysql:8

ENV TZ=Asia/Shanghai
RUN ln -sf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

COPY rose.sql /docker-entrypoint-initdb.d
COPY nacos-2.5.1.sql /docker-entrypoint-initdb.d
COPY xxl_job.sql /docker-entrypoint-initdb.d
