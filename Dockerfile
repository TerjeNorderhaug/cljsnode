FROM clojure:lein-2.7.0 

RUN apt-get update -y
RUN mkdir /nodejs && \
    curl https://nodejs.org/dist/v6.6.0/node-v6.6.0-linux-x64.tar.gz \
    | tar xvzf - -C /nodejs --strip-components=1
ENV PATH $PATH:/nodejs/bin

EXPOSE 1337

RUN mkdir -p /usr/src/app
COPY . /usr/src/app
WORKDIR /usr/src/app

RUN lein do deps, compile
CMD ["lein", "run"]



