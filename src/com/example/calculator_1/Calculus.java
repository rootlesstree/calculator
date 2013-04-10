package com.example.calculator_1;

public class Calculus {
	private Parser parser;
	
	//Operator list:
	private final static char plus='+', minus='-', multiply='*', div='/', power = '^', root='#';
	
	//Constructors:
	public Calculus()
	{
		parser = new Parser();
	};
	
	public double Integrate(String func, double a, double b, long c)
	{
		StringBuilder funcBuilder = new StringBuilder(func);
		solveFunctionInside(funcBuilder);
		func = funcBuilder.toString();
		
		//for debugging
		//System.out.println("Integrate func:" + func);
		
		//temporary
		long n = 1000*c;
		double x;
		double dx = (b-a)/n;
		
		double result = 0;
		for (long i=0; i<n; i++)
		{
			x = a + dx*i;
			double fa =  Double.parseDouble(parser.parse(func.replace("X", "("+Double.toString(x)+")")));
			double fmid = Double.parseDouble(parser.parse(func.replace("X", "("+Double.toString(x+dx/2)+")")));
			double fb = Double.parseDouble(parser.parse(func.replace("X", "("+Double.toString(x+dx)+")")));
			
			result += (fa + 4*fmid + fb)*dx/6;
		}
		
		return result;
		
	}
	
	public double Differentiate(String func, double a)
	{
		StringBuilder funcBuilder = new StringBuilder(func);
		solveFunctionInside(funcBuilder);
		func = funcBuilder.toString();
		
		//for debugging
		//System.out.println("Differentiate func:" + func);
		
		double dx = a*0.5e-2;
		if (dx<0)
			dx *= -1;
		if (dx<0.5e-2)
			dx = 0.5e-2;
		
		//double result = dif5Point(func, a, dx);
		double result = difCFD5(func, a , dx);
		
		return result;
	}
	
	private double dif5Point(String func, double a, double dx)
	{
		//double fx = Double.parseDouble(parser.parse(func.replace("X", "("+Double.toString(a)+")")));
		double fxm2h = Double.parseDouble(parser.parse(func.replace("X", "("+Double.toString(a-2*dx)+")")));
		double fxm1h = Double.parseDouble(parser.parse(func.replace("X", "("+Double.toString(a-dx)+")")));
		double fxp2h = Double.parseDouble(parser.parse(func.replace("X", "("+Double.toString(a+2*dx)+")")));
		double fxp1h = Double.parseDouble(parser.parse(func.replace("X", "("+Double.toString(a+dx)+")")));
		
		//debugging
		
		double result = (-fxp2h + 8*fxp1h - 8*fxm1h + fxm2h)/(12*dx);
		return result;
	}
	
	private double difCFD5(String func, double a, double dx)
	{
		//double fx = Double.parseDouble(parser.parse(func.replace("X", "("+Double.toString(a)+")")));
		double fxm4h = Double.parseDouble(parser.parse(func.replace("X", "("+Double.toString(a-4*dx)+")")));
		double fxm3h = Double.parseDouble(parser.parse(func.replace("X", "("+Double.toString(a-3*dx)+")")));
		double fxm2h = Double.parseDouble(parser.parse(func.replace("X", "("+Double.toString(a-2*dx)+")")));
		double fxm1h = Double.parseDouble(parser.parse(func.replace("X", "("+Double.toString(a-dx)+")")));
		double fxp4h = Double.parseDouble(parser.parse(func.replace("X", "("+Double.toString(a+4*dx)+")")));
		double fxp3h = Double.parseDouble(parser.parse(func.replace("X", "("+Double.toString(a+3*dx)+")")));
		double fxp2h = Double.parseDouble(parser.parse(func.replace("X", "("+Double.toString(a+2*dx)+")")));
		double fxp1h = Double.parseDouble(parser.parse(func.replace("X", "("+Double.toString(a+dx)+")")));
		
		//debugging
		
		double result = ((fxm4h - fxp4h)*3 - (fxm3h-fxp3h)*32 + (fxm2h-fxp2h)*168 - (fxm1h-fxp1h)*672)/dx/840;
		//double result = (fxp1h-fxm1h)/dx/2;
		return result;
	}
	
	private void solveFunctionInside(StringBuilder funcBuilder)
	{
		String func = funcBuilder.toString();
		
		for (int i=0; i<func.length()-3; i++)
		{
			if (func.substring(i, i+4).compareTo("Idx(")==0 ||
					func.substring(i, i+4).compareTo("Ddx(")==0)
			{
				int ct = i+4;
				int bracket_count = 1;
				while (bracket_count>0)
				{
					ct++;
					if (func.charAt(ct)=='(')
						bracket_count++;
					else if (func.charAt(ct)==')')
						bracket_count--;
				}
				
				double tempResult = parser.parsePrimaryExpression(func.substring(i, ct+1));
				int tempResultSize = Double.toString(tempResult).length() + 2;
				
				func = func.substring(0, i) + "(" + Double.toString(tempResult) + ")" +
						func.substring(ct+1, func.length());
				
				i += tempResultSize;
				
			}
		}
		
		funcBuilder.setLength(0);
		funcBuilder.append(func);
	}
	/*
	private String dif(String func)
	{
		return difAdd(func);
	}
	
	private String difAdd(String func)
	{
		char op=plus;
		int m_cur = readMultiplicativeExpression(func, 0);
		while (nextChar(plus) || nextChar(minus))
		{
			op = str.charAt(m_cur);
			m_cur++;
			right = readMultiplicativeExpression(func, m_cur);
			if (op == plus) left += right;
			else if (op == minus) left -= right;
		}
		return left;
	}
	
	//Parsing function:
	private double readExpression(String str)
	{
		return readAdditiveExpression(String str);
	}
	
	private double readAdditiveExpression()
	{
		double left=0, right=0;
		char op=plus;
		left = readMultiplicativeExpression();
		while (nextChar(plus) || nextChar(minus))
		{
			op = str.charAt(m_cur);
			m_cur++;
			right = readMultiplicativeExpression();
			if (op == plus) left += right;
			else if (op == minus) left -= right;
		}
		return left;
	}
	
	*/
}


