FROM debian:jessie
MAINTAINER Firespring "info.dev@firespring.com"

# Add add-apt-repository
RUN apt-get update && \
    apt-get install -y software-properties-common

RUN add-apt-repository "deb http://http.debian.net/debian jessie-backports main" && \
    apt-get update && \
    apt-get install -t jessie-backports openjdk-8-jdk -y

RUN apt-get update && apt-get install -y curl tar

# Add JRuby
ENV JRUBY_VERSION 9.1.2.0
ENV JRUBY_SHA256 60598a465883ab4c933f805de4a7f280052bddc793b95735465619c03ca43f35
RUN mkdir /opt/jruby \
  && curl -fSL https://s3.amazonaws.com/jruby.org/downloads/${JRUBY_VERSION}/jruby-bin-${JRUBY_VERSION}.tar.gz -o /tmp/jruby.tar.gz \
  #&& echo "$JRUBY_SHA256 /tmp/jruby.tar.gz" | sha256sum -c - \
  && tar -zx --strip-components=1 -f /tmp/jruby.tar.gz -C /opt/jruby \
  && rm /tmp/jruby.tar.gz

ENV PATH /opt/jruby/bin:$PATH

RUN mkdir /usr/src/code
WORKDIR /usr/src/app

# Copy in the gradle project
COPY . /usr/src/app

# Add JRuby to the project
RUN mkdir /usr/src/app/lib/
RUN cp /opt/jruby/lib/jruby.jar /usr/src/app/lib/jruby.jar
RUN cp -r /opt/jruby/lib/ruby/stdlib/ src/main/resources/
RUN touch /usr/src/app/src/main/resources/stdlib/.jrubydir

# Do a gradlew build, which also fetches gradle for the container
RUN ./gradlew build

COPY ./Docker/script/build_project /script/build_project
ENTRYPOINT ["/script/build_project"]
