package com.autosmsreply.assets;

/**
 * Holder class for autoreplys. 
 * @author Jocke
 *
 */
public class AutoReply {
	public int id;
	public String autoReplyName;
	public String autoReplyMessage;
	public boolean autoReplyStatus;
	
	public AutoReply(int id,String autoReplyName,String autoReplyMessage,boolean autoReplyStatus){
		this.id = id;
		this.autoReplyName = autoReplyName;
		this.autoReplyMessage = autoReplyMessage;
		this.autoReplyStatus = autoReplyStatus;
	}
}
