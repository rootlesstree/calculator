package com.example.calculator_1;


import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {

	public static String monitor1Text = "", monitor2Text = "";
	TextView monitor1, monitor2;
	Parser parser;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		monitor1 = (TextView) findViewById(R.id.monitor1);
		monitor1.setText(monitor1Text);
		monitor2 = (TextView) findViewById(R.id.monitor2);
		monitor2.setText(monitor2Text);
		parser = new Parser();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	public void refreshText()
	{
		monitor1.setText(monitor1Text);
		monitor2.setText(monitor2Text);
	}
	
	public void add(View view)
	{
		if (monitor1Text.length() <= 200  )
		{
			monitor1Text = monitor1Text+view.getTag();
			refreshText();
		}
	}
	
	public void del(View view)
	{
		System.out.println("Start del");
		if (monitor1Text.length() > 0)
		{
			monitor1Text = monitor1Text.substring(0,monitor1Text.length()-1);
			refreshText();
		}
	}
	
	public void AC(View view)
	{
			monitor1Text = "";
			monitor2Text = "";
			refreshText();
	}
	
	public void equal(View view)
	{
		try{
		monitor2Text = "" + Math.rint(Double.parseDouble(parser.parse(monitor1Text))*Math.pow(10,12))/Math.pow(10, 12);
		}
		catch(RuntimeException e)
		{
			monitor2Text = "Syntax Error";
		}
		monitor1Text = "";
		refreshText();
	}
	

}
