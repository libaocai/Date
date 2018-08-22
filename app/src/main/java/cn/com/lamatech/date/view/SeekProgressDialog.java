package cn.com.lamatech.date.view;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

import cn.com.lamatech.date.R;


public class SeekProgressDialog extends Dialog implements Runnable{


	private String mFinishString;//完成时候提示的文字
	private TextView mTipTextView;//提示的TextView
	private int mFinishDelayTime;//完成显示动画时间
	public SeekProgressDialog(Context context){
		this(context, R.style.custom_dialog);
	}

	public SeekProgressDialog(Context context, int theme) {
		super(context, theme);
		this.setContentView(R.layout.dialog_circle_process_tip);
		this.setCancelable(true);
		this.setCanceledOnTouchOutside(false);
		this.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
	
		initData();
	}
	
	private void initData(){
		mTipTextView =(TextView)findViewById(R.id.TextView);
		mTipTextView.setText("加载中...");
		mFinishDelayTime = 800;
		mFinishString = "完成";
	}
	/**
	 * 设置正在运行时候的提示文字
	 * @param tip
	 */
	public void setTipText(String tip){
		mTipTextView.setText(tip);
	}
	/**
	 * 设置结束的时候提示的文字
	 * @param tip
	 */
	public void setFinishTipText(String tip){
		this.mFinishString = tip;
	}
	
	/**
	 * 关闭
	 */
	public void close(){
		this.setContentView(R.layout.dialog_circle_tip);
		mFinishString = "完成";
		((TextView)findViewById(R.id.TextView)).setText(mFinishString);
		this.dismiss();
//		new Thread(this).start();
	}
	/**
	 * 设置完成时候动画延时的时间，单位是1000 == 1秒
	 * 默认是 800
	 * @param finishDelayTime
	 */
	public void setFinishDelay(int finishDelayTime){
		mFinishDelayTime = finishDelayTime;
	}
	@Override
	public void run() {
		try {
			Thread.sleep(mFinishDelayTime);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.dismiss();
	}
	
	
}
