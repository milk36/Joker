package com.testdb;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;

import com.config.Config;
import com.db.DbBase;
import com.db.PlayerDB;

public class PlayerDBTest {
	private static Logger _log = Logger.getLogger(PlayerDBTest.class);

	public static void main(String[] args) {

		Config.init();

		DbBase gamedb = new DbBase("gamedb");
		List<PlayerDB> ls;
		try {
			ls = gamedb.query(PlayerDB.class, "select * from user_info ");
			System.out.println("playerdb size:" + ls.size());

			String a1_uuid = "c2b4e32e-8fbf-469f-9dfe-768b6021fbe6";
			for (int i = 0; i < 10; i++) {
				ls = gamedb.query(PlayerDB.class, "select * from user_info where id=?", a1_uuid);
				PlayerDB player = ls.get(0);

				player.setMp(player.getMp() + 1);
				long start = System.currentTimeMillis();
				gamedb.update(player, "update user_info ", "where id=?", player.getId());
				_log.info("Time-consuming:" + (System.currentTimeMillis() - start));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
}
