package cn.javastack.domain.v5.fetcher;

public interface ItemFetcher<RESULT> {
    Long getFetchId();

    void setResult(RESULT address);
}
