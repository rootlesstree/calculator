package com.example.calculator_1;

public class Parser {
	//Variables (public)
	private String str;
	private int m_cur, m_end;
	private double result = 0;
	
	//Operator list:
	private final static char plus='+', minus='-', multiply='*', div='/', power = '^', root='#';
	
	//Constructors:
	public Parser(){};
	
	//Public Function:
	public String parse(String s)
	{
		//for debugging
		//System.out.println("parse s:"+s);
				
		str = s;
		m_cur = 0;
		m_end = str.length();
		result = readExpression();
		//We rounding the result to use least digits
		//result = Math.rint(result*Math.pow(10,12))/Math.pow(10, 12);
		return Double.toString(result);
	}
	
	public double parsePrimaryExpression(String s)
	{
		//for debugging
		//System.out.println("parse Primary s:"+s);
		
		str = s;
		m_cur = 0;
		m_end = str.length();
		result = readPrimaryExpression();
	//	result = Math.rint(result*Math.pow(10,12))/Math.pow(10, 12);
		return result;
	}
	
	//public Function:
	//Helper function:
	private boolean nextChar(char c)//
	{
		if (m_cur < m_end && str.charAt(m_cur) == c) return true;
		else return false;
	}
	
	private boolean nextCharIsAlphabet()
	{
		if (m_cur < m_end && ((str.charAt(m_cur) >= 'a' && str.charAt(m_cur) <='z') || (str.charAt(m_cur) >= 'A' && str.charAt(m_cur) <='Z')) )
			return true;
		else return false;
	}
	
	private boolean mayExpectChar(char c)//
	{
		if (nextChar(c))
		{
			m_cur++;
			return true;
		}
		else return false;
	}
	
	private boolean expectChar(char c)
	{
		if (mayExpectChar(c))
		{
			return true;
		}
		else
		{
			assert str.charAt(m_cur) == c : "Expect a certain character";
			throw new RuntimeException("Expect a certain character");
			//somehow the assert don't stop the program
			//return false;
		}
	}
	
	private boolean nextCharIsDigit()//
	{
		if ( m_cur < m_end &&  str.charAt(m_cur) >= '0' && str.charAt(m_cur) <= '9') return true;
		else return false;
	}
	
	private boolean nextString(String s)//
	{
		if (m_end - m_cur >= s.length() && str.substring(m_cur, m_cur + s.length()).equals(s)) return true;
		else return false;
	}
	
	private boolean mayExpectString(String s)
	{
		if (nextString(s))
		{
			m_cur += s.length();
			return true;
		}
		else return false;
	}
	

	
	//Parsing function:
	private double readExpression()
	{
		return readAdditiveExpression();
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
	
	private double readMultiplicativeExpression()
	{
		double left=0, right=1;
		char op=multiply;
		left = readPowerExpression();
		if (nextCharIsDigit()) throw new RuntimeException("test");
		while ((nextChar(multiply) || nextChar(div) || nextChar('%') || nextChar('(') || nextCharIsAlphabet() ) && !nextChar(power) && !nextChar(root) )
		{
			if (nextChar(multiply) || nextChar(div))
			{
				op = str.charAt(m_cur);
				m_cur++;
			}
			else op = multiply;
			//In case where no multiplicative symbol occured, we treat it as multiplication
			right = readPowerExpression();
			if (op == multiply) left *= right;
			else if (op == div) left /= right;
			else if (op == '%') left %= right;
			//System.out.println("Char=" + op);
		}
		return left;
	}
	
	private double readPowerExpression()
	{
		double left=0, right=1;
		char op=power;
		left = readUnaryExpression();
		while (nextChar(power) || nextChar(root) )
		{
			op = str.charAt(m_cur);
			m_cur++;
			right = readUnaryExpression();
			if (op == power) left = Math.pow(left, right);
			else if (op == root) left = Math.pow(right,1/left);
		}
		return left;
	}
	
	private double readUnaryExpression()
	{
		if (mayExpectChar(minus))
		{
			return -readUnaryExpression();
		}

		else
		{
			return readPrimaryExpression();
		}
	}
	
	private double readPrimaryExpression()
	{
		Calculus calculus = new Calculus();
		if (mayExpectChar('('))
		{
			double tempResult = 0;
			tempResult = readExpression();
			expectChar(')');
			return tempResult;
		}
		//Put another functions here:
		else if (mayExpectString("log"))
		{
			double tempResult = 0;
			/*
			expectChar('(');
			tempResult = Math.log10(readExpression());
			expectChar(')');
			*/
			//other way to do this:
			tempResult = Math.log10(readPrimaryExpression());
			return tempResult;
		}
		else if (mayExpectString("sin"))
		{
			double tempResult = 0;
			tempResult = Math.sin(readPrimaryExpression());
			return tempResult;
		}
		else if (mayExpectString("cos"))
		{
			double tempResult = 0;
			tempResult = Math.cos(readPrimaryExpression());
			return tempResult;
		}
		else if (mayExpectString("tan"))
		{
			double tempResult = 0;
			tempResult = Math.cos(readPrimaryExpression());
			return tempResult;
		}
		else if (mayExpectString("ln"))
		{
			double tempResult = 0;
			tempResult = Math.log(readPrimaryExpression());
			return tempResult;
		}
		else if (mayExpectString("e"))
		{
			double tempResult = 0;
			tempResult = Math.E;
			return tempResult;
		}
		else if (mayExpectString("Idx"))
		{
			double tempResult = 0;
			StringBuilder func = new StringBuilder();
			StringBuilder a = new StringBuilder(),b = new StringBuilder(),c = new StringBuilder();
			readIntegrationExpression(func,a,b,c);
			if (c.length()==0) c.append(3);
			//for debugging
			//System.out.println("In Idx::func:"+func + " ,a:" + a + " ,b:" + b+ " ,c:" + c);
			//Call the function to integrate
			tempResult = calculus.Integrate(func.toString(),Double.parseDouble(a.toString()),Double.parseDouble(b.toString()),Long.parseLong(c.toString()));
			return tempResult;	
		}
		else if (mayExpectString("Ddx"))
		{
			double tempResult = 0;
			StringBuilder func = new StringBuilder();
			StringBuilder a = new StringBuilder();
			readDifferentiationExpression(func,a);
			//for debugging
			//System.out.println("In Ddx::func:" + func + " ,a:" + a );
			//Call the function to differentiate
			tempResult = calculus.Differentiate(func.toString(),Double.parseDouble(a.toString()));
			return tempResult;
		}
		else
		{
			return readNumber();
		}
	}
	
	private void readDifferentiationExpression(StringBuilder func, StringBuilder a)
	{
		if (expectChar('('))
		{
			int funcEnd = getFunctionEnd();
			func.append(str.substring(m_cur,funcEnd));
			m_cur = funcEnd;
			expectChar(',');
			a.append(readNumber());
			expectChar(')');
			return;
		}
		else throw new RuntimeException("Must be open bracket after differentiation!");
	}
	
	private void readIntegrationExpression(StringBuilder  func, StringBuilder a, StringBuilder b, StringBuilder c)
	{
		if (expectChar('('))
		{
			int funcEnd=m_cur;
			/*
			while (funcEnd < m_end && !( str.charAt(funcEnd)== ',' && bracketCount==0) )
			{
				if (str.charAt(funcEnd)=='(') bracketCount++;
				else if (str.charAt(funcEnd)==')') bracketCount--;
				if (bracketCount < 0) throw new RuntimeException("Unbalanced Bracket in the integral!");
				funcEnd++;			
			}
			if (bracketCount > 0) throw new RuntimeException("Unbalanced Bracket in the integral!");
			*/
			funcEnd = getFunctionEnd();
			func.append(str.substring(m_cur,funcEnd));
			m_cur = funcEnd;
			expectChar(',');
			a.append(readNumber());
			expectChar(',');
			b.append(readNumber());			
			if (mayExpectChar(')'))
			{
				c = new StringBuilder();
				return;
			}
			else
			{
				expectChar(',');
				double tempDouble = readNumber();
				if (Math.abs(Math.round(tempDouble)-tempDouble) > 0.000001   ) throw new RuntimeException("c must be integer!");
				c.append(Math.round(tempDouble));
				expectChar(')');
			}
			return;
		}
		else throw new RuntimeException("test");		
	}
	
	private int getFunctionEnd()
	{
		int funcEnd=m_cur;
		int bracketCount=0;
		while (funcEnd < m_end && !( str.charAt(funcEnd)== ',' && bracketCount==0) )
		{
			if (str.charAt(funcEnd)=='(') bracketCount++;
			else if (str.charAt(funcEnd)==')') bracketCount--;
			if (bracketCount < 0) throw new RuntimeException("Unbalanced Bracket in the function!");
			funcEnd++;			
		}
		if (bracketCount > 0) throw new RuntimeException("Unbalanced Bracket in the function!");
		return funcEnd;
	}
	
	private double readNumber()
	{
		String tempStr = "";
		double tempResult = 0;
		if (mayExpectChar('0'))
		{
			tempStr = "0";
			if (mayExpectChar('.'))
			{
				tempStr = tempStr+ "." + readDigits();
			}	
		}
		else if (mayExpectChar('.'))
		{
			tempStr = "0." + readDigits();
		}
		else
		{
			tempStr = tempStr + readDigits();
			if (mayExpectChar('.'))
			{
				tempStr = tempStr+ "." + readDigits();
			}	
		}
		tempResult = Double.parseDouble(tempStr);
		tempResult = readExponentPartOpt(tempResult);
		return tempResult;
	}
	
	private String readDigits()
	{
		assert nextCharIsDigit() : "Must read a digit!";
		String tempStr = "";
		int start = m_cur;
		while (nextCharIsDigit() && m_cur < m_end) m_cur++;
		tempStr = tempStr + str.substring(start, m_cur);
		return tempStr;
	}
	
	private double readExponentPartOpt(double d)
	{
		if (mayExpectChar('E'))
		{
			char sign = plus;
			if (nextChar(plus) || nextChar(minus))
			{
				sign = str.charAt(m_cur);
				m_cur++;
			}
			double tempDouble = Double.parseDouble(readDigits());
			if (sign == minus) tempDouble = -tempDouble;
			return d*Math.pow(10, tempDouble);
		}
		else return d;
	}
	
	
}
