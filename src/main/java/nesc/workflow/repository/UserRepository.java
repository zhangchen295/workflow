package nesc.workflow.repository;

import nesc.workflow.jpa.BaseJPA;
import nesc.workflow.model.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository
        extends BaseJPA<User> {

    //自定义repository。手写sql
    @Query(value = "update user set name=?1 where id=?4",nativeQuery = true)   //占位符传值形式
    @Modifying
    int updateById(String name,int id);

    @Query("from User u where u.username=:username")   //SPEL表达式
    User findUser(@Param("username") String username);// 参数username 映射到数据库字段username
}

