package com;

public class AppException extends Exception
{
	public AppException(){}
	public AppException(String gripe)
	{
		super(gripe);
	}
}
