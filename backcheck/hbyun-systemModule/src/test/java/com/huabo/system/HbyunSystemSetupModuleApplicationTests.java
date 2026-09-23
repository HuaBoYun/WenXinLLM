package com.huabo.system;

import com.hbfk.util.BaseDao;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@SpringBootTest
@Slf4j
class HbyunSystemSetupModuleApplicationTests {

	@Test
	void contextLoads() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String fid = null;
		String otherName = "开发平台";
		try {
			try {
				String sql = "SELECT F_Id FROM base_system WHERE F_FullName=" + "'" + otherName + "'";
				con = BaseDao.getInstance().getConnection();
				ps = con.prepareStatement(sql);
				rs = ps.executeQuery();
				while (rs.next()) {
					fid = rs.getString("F_Id");
				}
			} finally {
				BaseDao.getInstance().close(con, rs, ps);
			}
		} catch (Exception e) {
			log.error("获取第三方项目编码异常");
		}
		log.info("========================="+fid);
	}

}
