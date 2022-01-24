package com.bfxy.netty.struct;

public final class NettyMessage {

    private Header header;

    private Object body;

    /**
     * @return the header
     */
    public final Header getHeader() {
    	return header;
    }

    /**
     * @param header
     *            the header to set
     */
    public final void setHeader(Header header) {
    	this.header = header;
    }

    /**
     * @return the body
     */
    public final Object getBody() {
    	return body;
    }

    /**
     * @param body
     *            the body to set
     */
    public final void setBody(Object body) {
    	this.body = body;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
    	return "NettyMessage [header=" + header + "]";
    }
}
