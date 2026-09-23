package com.huabo.legal.exam.modules.user.book.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hbfk.util.JsonBean;
import com.huabo.legal.exam.core.api.dto.PagingReqDTO;
import com.huabo.legal.exam.modules.user.book.dto.UserBookDTO;
import com.huabo.legal.exam.modules.user.book.entity.UserBook;

/**
 * <p>
 * 错题本业务类
 * </p>
 *
 * @author 聪明笨狗
 * @since 2020-05-27 17:56
 */
public interface UserBookService extends IService<UserBook> {

	/**
	 * 分页查询数据
	 * @param reqDTO
	 * @return
	 */
	JsonBean paging(PagingReqDTO<UserBookDTO> reqDTO, String userId);

	/**
	 * 加入错题本
	 * @param quId
	 * @param examId
	 */
	void addBook(String examId, String quId, String userId);

	/**
	 * 查找第一个错题
	 * @param quId
	 * @param examId
	 * @return
	 */
	String findNext(String examId, String quId, String userId);
}
