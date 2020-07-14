package nesc.workflow.bean;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QUserBean is a Querydsl query type for UserBean
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QUserBean extends EntityPathBase<UserBean> {

    private static final long serialVersionUID = 1981838929L;

    public static final QUserBean userBean = new QUserBean("userBean");

    public final StringPath address = createString("address");

    public final NumberPath<Integer> age = createNumber("age", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final StringPath pwd = createString("pwd");

    public QUserBean(String variable) {
        super(UserBean.class, forVariable(variable));
    }

    public QUserBean(Path<? extends UserBean> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUserBean(PathMetadata metadata) {
        super(UserBean.class, metadata);
    }

}

