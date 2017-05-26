/*
 * RemoteException.java
 * classes : com.gumpcome.kernel.net.exception.RemoteException
 * @author 
 * V 1.0.0
 * Create at 2012-8-3 下午5:38:14
 */
package com.liberty.exception;

/**
 * com.gumpcome.kernel.net.exception.RemoteException
 *
 * @author <a href="mailto:libin09@baidu.com">李彬</a> <br/>
 *         服务器response的错误信息 create at 2012-8-3 下午5:38:14
 */
public class RemoteException extends Exception {
    /**
     * 序列号ID
     */
    private static final long serialVersionUID = -1460894893738016580L;

    /**
     * 错误代码
     */
    private int mErrorCode;

    public RemoteException(int errorCode, String errorMessage) {
        super(errorMessage);
        mErrorCode = errorCode;
    }

    /**
     * @return int 服务器返回的错误代码
     */
    public int getErrorCode() {
        return mErrorCode;
    }

    @Override
    public String getMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append("errorCode = ");
        sb.append(mErrorCode);
        sb.append(" ");
        sb.append(super.getMessage());
        return sb.toString();
    }
}
