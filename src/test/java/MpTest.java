import cn.hutool.core.date.DateTime;
import com.lead.fund.base.common.basic.cons.ds.DatabaseType;
import com.lead.fund.base.common.database.helper.DdlEntry;
import com.lead.fund.base.common.util.DateUtil;
import com.lead.fund.base.common.util.StrUtil;
import com.lead.fund.base.server.mp.entity.dmmp.MpSignInHistoryEntity;
import com.lead.fund.base.server.mp.entity.douson.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * TradeTest
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-03-28 20:02
 */
public class MpTest {

    @Test
    @DisplayName("test any...")
    void test() throws Exception {
    }

    @Test
    @DisplayName("test database...")
    void database() throws Exception {
        System.out.println(DateUtil.dateTime(DateTime.of(17408317980012L), com.lead.fund.base.common.basic.cons.util.date.DateFormat.DOT_MILLIS.getCode(), "--"));
        System.out.println(DateUtil.parse(DateUtil.dateTime(DateTime.of(17408317980012L), com.lead.fund.base.common.basic.cons.util.date.DateFormat.DOT_MILLIS.getCode(), "--")).getTime());
        System.out.println(new DdlEntry("15110").generateDdl(SchedulingEntity.class, DatabaseType.MYSQL));
        System.out.println(new DdlEntry("15110").generateDdl(SchedulingDetailEntity.class, DatabaseType.MYSQL));
    }
}
