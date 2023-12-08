package cn.javastack.domain.v6.fetcher;

public interface ItemFetcher<RESULT> {
    Long getFetchId();

    void setResult(RESULT result);
}
