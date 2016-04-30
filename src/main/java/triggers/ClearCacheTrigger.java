package triggers;

import com.fs.commons.dao.AbstractDao;
import com.fs.commons.dao.dynamic.meta.Record;
import com.fs.commons.dao.dynamic.trigger.TriggerAdapter;
import com.fs.commons.dao.exception.DaoException;

public class ClearCacheTrigger extends TriggerAdapter {
	@Override
	public void afterUpdate(Record oldRecord, Record newRecord) throws DaoException {
		AbstractDao.resetCache();
	}
}
