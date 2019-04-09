package com.moviewibe.app.dao.iDao;

import java.util.List;

import com.moviewibe.app.beans.AbstractBean;

/**
 * @author Tarcisio Machado dos Reis
 *
 */
public interface iBD {

	int update(AbstractBean bean);
	long insert(AbstractBean bean);
	int delete(AbstractBean bean);
	int delete();
	List<AbstractBean> search() throws Exception;
    List<AbstractBean> search(int id) throws Exception;

}
