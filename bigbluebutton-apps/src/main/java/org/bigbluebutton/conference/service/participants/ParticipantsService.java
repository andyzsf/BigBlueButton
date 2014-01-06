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

package org.bigbluebutton.conference.service.participants;

import org.slf4j.Logger;
import org.red5.logging.Red5LoggerFactory;
import org.red5.server.api.Red5;
import org.red5.server.api.scope.IScope;
import java.util.Map;
import org.bigbluebutton.conference.BigBlueButtonSession;
import org.bigbluebutton.conference.Constants;


public class ParticipantsService {

	private static Logger log = Red5LoggerFactory.getLogger( ParticipantsService.class, "bigbluebutton" );	
	private ParticipantsApplication application;
	private ParticipantsBridge participantsBridge;

	public void assignPresenter(Map<String, String> msg) {

		IScope scope = Red5.getConnectionLocal().getScope();
		log.debug("Checking assignPresenter values " + msg.get("newPresenterID") + " " + msg.get("newPresenterName") + " " + msg.get("assignedBy"));
		application.assignPresenter(scope.getName(), (String) msg.get("newPresenterID"), (String) msg.get("newPresenterName"), (String) msg.get("assignedBy"));
		/*ArrayList<String> presenter = new ArrayList<String>();
		presenter.add(userid);
		presenter.add(name);
		presenter.add(assignedBy.toString());
		ArrayList<String> curPresenter = application.getCurrentPresenter(scope.getName());
		application.setParticipantStatus(scope.getName(), userid, "presenter", true);
		
		if (curPresenter != null){ 
			String curUserid = (String) curPresenter.get(0);
			if (! curUserid.equals(userid)){
				log.info("Changing the current presenter [" + curPresenter.get(0) + "] to viewer.");
				application.setParticipantStatus(scope.getName(), curPresenter.get(0), "presenter", false);
			}
		} else {
			log.info("No current presenter. So do nothing.");
		}*/
		//application.assignPresenter(scope.getName(), presenter);
		
		ArrayList<String> curPresenter = application.getCurrentPresenter(scope.getName());
		String previousPresenter = null;
		if (curPresenter != null){ 
			String curUserid = (String) curPresenter.get(0);
			if (! curUserid.equalsIgnoreCase(userid)){
				previousPresenter = curUserid;
			}
		} else {
			log.info("No current presenter. So do nothing.");
		}
		participantsBridge.storeAssignPresenter(scope.getName(), userid, previousPresenter);
		
		participantsBridge.sendAssignPresenter(scope.getName(), userid);
	}
	
	public void getParticipants() {
		IScope scope = Red5.getConnectionLocal().getScope();
		application.getUsers(scope.getName(), getBbbSession().getInternalUserID());
	}
	
		
	public void setParticipantStatus(Map<String, Object> msg) {
		String roomName = Red5.getConnectionLocal().getScope().getName();

		application.setParticipantStatus(roomName, (String) msg.get("userID"), (String) msg.get("status"), (Object) msg.get("value"));
	}
	
	public void setParticipantsApplication(ParticipantsApplication a) {
		log.debug("Setting Participants Applications");
		application = a;
	}
	
	private BigBlueButtonSession getBbbSession() {
		return (BigBlueButtonSession) Red5.getConnectionLocal().getAttribute(Constants.SESSION);
	public void setParticipantsBridge(ParticipantsBridge pb){
		this.participantsBridge = pb;
	}
}
