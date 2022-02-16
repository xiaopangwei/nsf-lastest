FROM openjdk:8u222

ENV TZ=Asia/Shanghai LANG=C.UTF-8 LANGUAGE=C.UTF-8 LC_ALL=C.UTF-8

RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

RUN apt-get update && apt-get install -y unzip wget nano vim expat net-tools less netcat lsof

RUN mkdir -p /opt/nsf && cd /opt/nsf && \
    wget "http://console-cust92.netease.com/download/nsf/nsf-agent-v2.6.8-c2a5e1f9-20201203-141827.jar" -O nsf-agent.jar

ADD ./*/target/classes/nsf-cust92.yml /opt/nsf/

RUN mkdir -p /opt/apm && cd /opt/apm && \
    wget "http://qingzhou-agent.nos-eastchina1.126.net/napm-java-agent-v5.4.1-200511-09ff9784.tar.gz" -O napm-java-agent.tar.gz  && \
    tar zxvf napm-java-agent.tar.gz

ADD ./*/target/classes/napm-agent.properties /opt/apm/

ADD ./*/target/nsf*.jar /app.jar

ENTRYPOINT ["java","-Xms512m","-Xmx1024m","-Dserver.port=8080","-Dstock_viewer_url=http://hw-stock-viewer", "-Dstock_provider_url=http://hw-stock-provider", "-Dstock_advisor_url=http://hw-stock-advisor", "-Dnsf.prometheus.enable=true","-javaagent:/opt/nsf/nsf-agent.jar=nsf-cust92","-javaagent:/opt/apm/napm-java-agent/napm-java-rewriter.jar=conf=napm-agent.properties","-jar","/app.jar"]
