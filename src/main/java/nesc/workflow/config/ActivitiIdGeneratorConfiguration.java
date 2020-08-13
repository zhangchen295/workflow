package nesc.workflow.config;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import org.activiti.engine.impl.cfg.IdGenerator;
import org.springframework.stereotype.Component;

/**
 * 自定义id策略
 *
 * @since 2020/3/4
 */
@Component
public class ActivitiIdGeneratorConfiguration implements IdGenerator {

    @Override
    public String getNextId() {
        return IdWorker.getIdStr();
    }
}
