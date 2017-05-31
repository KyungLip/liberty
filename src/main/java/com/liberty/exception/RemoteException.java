
package com.liberty.exception;

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
