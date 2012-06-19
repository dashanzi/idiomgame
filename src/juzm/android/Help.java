package juzm.android;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * 菜单
 * @author juzhaoming
 * @version v 1.0 2012-06-18
 *
 */
public class Help extends Activity {
	private TextView text = null;
	private TextView decriptionContent = null;
	private TextView signature = null;
	private TextView copyRight = null;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.help);

		text = (TextView) findViewById(R.id.aboutTextViewId);
		text.setText("简介");
		
		String tab = "\n";
		decriptionContent = (TextView)findViewById(R.id.textView1);
		decriptionContent.setText("	这是一个管理日常收支的理财小工具，功能包括：" +tab+
				"	(1)支出:记录每笔支出情况" +tab+
				"	(2)收入:记录每笔收入情况" +tab+
				"	(3)明细:查看每笔收入明细，在长按某笔记录情况下，可对其进行明细查看、修改、删除操作" +tab+
				"	(4)统计:统计支出、收入、结余情况" +tab+
				"	(5)导入/导出：将数据导出至SD卡进行数据备份、从SD卡导入数据" +tab+
				"	(6)计算器：简易计算功能,结果可以作为一笔支出或收入的金额" +tab+tab+
				"	该软件较为简单，需要不断地升级完善，个人打分一颗星！");
		
		signature = (TextView)findViewById(R.id.textView2);
		signature.setText("— Mr. Ju");
		
		copyRight = (TextView) findViewById(R.id.textView3);
		copyRight.setText("Copyright (C) 2012 - forever. BigBusyMan");
	}
	
}
