FROM alpine:3.14

ENV HOME_EX=/ecom-market-test-task

WORKDIR $HOME_EX

COPY ./ecom-market-test-task $HOME_EX

CMD ["./run.sh"]