package nesc.workflow.utils;

import org.springframework.stereotype.Component;

@Component
public class CommonUtil {
    /**
     * firstResult指的是第一条,并不是第几页,
     * 第几条就像mysql分页一样,重0开始,maxResults就像pageSize,重0开始往后第10条.
     * 第二页则是(2-1)*10 ,10 就是重第十条开始,往后第十条.
     * 所以应该写为:(pageNum - 1) * pageSize, pageSize
     * @param firstResult
     * @param maxResults
     * @return
     */
    public int listPagedTool(int firstResult, int maxResults){
        return (firstResult - 1) * maxResults;
    }
}
