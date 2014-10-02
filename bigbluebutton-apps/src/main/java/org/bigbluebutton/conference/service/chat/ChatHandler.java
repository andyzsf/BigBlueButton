/**
* BigBlueButton open source conferencing system - http://www.bigbluebutton.org/
* 
* Copyright (c) 2012 BigBlueButton Inc. and by respective authors (see below).
*
* This program is free software; you can redistribute it and/or modify it under the
* terms of the GNU Lesser General Public License as published by the Free Software
* Foundation; either version 3.0 of the License, or (at your option) any later
* version.
* 
* BigBlueButton is distributed in the hope that it will be useful, but WITHOUT ANY
* WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
* PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public License along
* with BigBlueButton; if not, see <http://www.gnu.org/licenses/>.
*
*/

package org.bigbluebutton.conference.service.chat;

import org.red5.server.adapter.IApplication;
import org.red5.server.api.IClient;
import org.red5.server.api.IConnection;
import org.slf4j.Logger;
import org.red5.logging.Red5LoggerFactory;
import org.red5.server.api.scope.IScope;
import org.bigbluebutton.conference.service.recorder.RecorderApplication;

public class ChatHandler implements IApplication{
	private static Logger log = Red5LoggerFactory.getLogger( ChatHandler.class, "bigbluebutton" );

	private RecorderApplication recorderApplication;
	private ChatApplication chatApplication;

	private static final String APP = "CHAT";
	
	@Override
	public boolean appConnect(IConnection conn, Object[] params) {
		return true;
	}

	@Override
	public void appDisconnect(IConnection conn) {
	}

	@Override
	public boolean appJoin(IClient client, IScope scope) {
		return true;
	}

	@Override
	public void appLeave(IClient client, IScope scope) {
	}

	@Override
	public boolean appStart(IScope scope) {
		return true;
	}

	@Override
	public void appStop(IScope scope) {
	}
	
	@Override
	public void roomDisconnect(IConnection connection) {
	}

	@Override
	public boolean roomJoin(IClient client, IScope scope) {
		return true;
	}

	@Override
	public void roomLeave(IClient client, IScope scope) {
	}

	@Override
	public boolean roomConnect(IConnection connection, Object[] params) {		
		return true;
	}

	@Override
	public boolean roomStart(IScope scope) {
    	return true;
	}

	@Override
	public void roomStop(IScope scope) {
	}
	
	public void setChatApplication(ChatApplication a) {
		chatApplication = a;
	}
	
	public void setRecorderApplication(RecorderApplication a) {
		recorderApplication = a;
	}
}