FROM python:3.10-slim-buster

RUN apt-get clean && apt-get update && apt-get upgrade -y \
    && apt-get install --no-install-recommends -qy locales tzdata apt-utils apt-transport-https lsb-release gnupg software-properties-common build-essential vim jq zsh groff git curl wget zip unzip httpie \
    && locale-gen en_GB.UTF-8 \
    && ln -fs /usr/share/zoneinfo/Europe/London /etc/localtime \
    && dpkg-reconfigure -f noninteractive tzdata \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/* /.cache/*

RUN pip install --upgrade pip \
    && pip install --no-cache-dir awscli aws-shell awscli-login boto3 botocore wheel urllib3

RUN echo "deb http://apt.postgresql.org/pub/repos/apt $(lsb_release -cs)-pgdg main" > /etc/apt/sources.list.d/pgdg.list \
    && wget --quiet -O - https://www.postgresql.org/media/keys/ACCC4CF8.asc | apt-key add - \
    && apt-get update \
    && apt-get -y install postgresql-client

RUN addgroup --gid 2000 --system appgroup && \
    adduser --uid 2000 --system appuser --gid 2000



USER 2000
COPY migrationRun.sh disable_foreign_keys.sql enable_foreign_keys.sql /
CMD /bin/bash