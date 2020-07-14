package nesc.workflow.controller;

import com.querydsl.jpa.impl.JPAQueryFactory;
import nesc.workflow.bean.QUserBean;
import nesc.workflow.bean.UserBean;
import nesc.workflow.jpa.UserJPA;
import nesc.workflow.model.User;
import nesc.workflow.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET,value = "/delete/{id}")
    public void delete(@PathVariable("id")int id){
        userService.delete(id);
    }

    @RequestMapping(method = RequestMethod.POST,value = "/insert")
    public void insert(User user){
        userService.insert(user);
    }
    @RequestMapping(method = RequestMethod.POST,value = "/update/{id}")
    public void update(@RequestParam User user){
        userService.update(user);
    }

    @RequestMapping(method = RequestMethod.GET,value = "/{id}/select")
    public User select(@PathVariable("id")int id){
        return userService.selectById(id);
    }

//    @RequestMapping(method = RequestMethod.GET,value = "/selectAll/{pageNum}/{pageSize}")
//    public List<User> selectAll(@PathVariable("pageNum") int pageNum,
//                                @PathVariable("pageSize") int pageSize){
//        //另一种分页方法：Page<User> datas = userRepository.findAll(PageableTools.basicPage(1, 5, new SortDto("id")));
//        Iterator<User> userIterator = userService.selectAll(pageNum, pageSize);
//        List<User> list = new ArrayList<>();
//        while(userIterator.hasNext()){
//            list.add(userIterator.next());
//        }
//        return list;
//    }
    @Autowired
    private UserJPA userJPA;
    //实体管理者
    @Autowired
    private EntityManager entityManager;

    //JPA查询工厂
    private JPAQueryFactory queryFactory;

    @PostConstruct
    public void initFactory()
    {
        queryFactory = new JPAQueryFactory(entityManager);
        System.out.println("init JPAQueryFactory successfully");
    }

    /**
     * 查询全部数据并根据id倒序
     * @return
     */
    @RequestMapping(value = "/queryAll")
    public List<UserBean> queryAll()
    {
        //使用querydsl查询
        QUserBean _Q_user = QUserBean.userBean;
        //查询并返回结果集
        return queryFactory
                .selectFrom(_Q_user)//查询源
                .orderBy(_Q_user.id.desc())//根据id倒序
                .fetch();//执行查询并获取结果集
    }

    /**
     * 查询详情
     * @param id 主键编号
     * @return
     */
    @RequestMapping(value = "/detail/{id}")
    public UserBean detail(@PathVariable("id") Long id)
    {
        //使用querydsl查询
        QUserBean _Q_user = QUserBean.userBean;
        //查询并返回结果集
        return queryFactory
                .selectFrom(_Q_user)//查询源
                .where(_Q_user.id.eq(id))//指定查询具体id的数据
                .fetchOne();//执行查询并返回单个结果
    }
    /**
     * SpringDataJPA & QueryDSL实现单数据查询
     * @param id
     * @return

    @RequestMapping(value = "/detail_2/{id}")
    public Optional<UserBean> detail_2(@PathVariable("id") Long id)
    {
        //使用querydsl查询
        QUserBean _Q_user = QUserBean.userBean;

        //查询并返回指定id的单条数据
        return userJPA.findOne(_Q_user.id.eq(id));
    }*/
}

