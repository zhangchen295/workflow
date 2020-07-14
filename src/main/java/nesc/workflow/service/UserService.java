package nesc.workflow.service;

import nesc.workflow.model.User;

public interface UserService {
    /** 删除 */
    public void delete(long id);
    /** 增加*/
    public void insert(User user);
    /** 更新*/
    public int update(User user);
    /** 查询单个*/
    public User selectById(long id);
    /** 查询全部列表*/
  //  public Iterator<User> selectAll(int pageNum, int pageSize);
}
