FROM clojure:lein AS build-env
RUN mkdir /app
COPY ./project.clj /app/project.clj
WORKDIR /app
RUN lein deps
COPY . /app
RUN mv "$(lein ring uberjar | sed -n 's/^Created \(.*standalone\.jar\)/\1/p')" app-standalone.jar

FROM gcr.io/distroless/java17-debian12
COPY --from=build-env /app/app-standalone.jar /app/main.jar
WORKDIR /app
EXPOSE 5000
CMD ["main.jar"]