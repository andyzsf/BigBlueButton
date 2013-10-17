function addSyscheckNotice(priority, title, message) {
  var notice = '<div class="syscheck-callout syscheck-callout-' + priority + '">';
  if (title) {
    notice += '<h4>' + title + '</h4>';
  }
  notice += message + '</div>';

  var container = document.getElementById('syscheck-' + priority + '-container');
  container.insertAdjacentHTML('beforeend', notice);

  var header = container.firstElementChild;
  if (header && header.tagName == 'H3') {
    header.style.display = 'block';
  }
}

var registerListeners = function() {
  console.log("Listening for events.");
  BBBCheck.listen("MicCheckAppReadyEvent", function() {
    console.log("Received [MicCheckAppReadyEvent].");
  });
  BBBCheck.listen("NewRoleEvent", function(bbbCheckEvent) {
    console.log("New Role Event [amIPresenter=" + bbbCheckEvent.amIPresenter + ",role=" + bbbCheckEvent.role + ",newPresenterUserID=" + bbbCheckEvent.newPresenterUserID + "].");
  });
}

function bbbCheckFlashVersion(minVersion, callback) {
  var playerVersion = BBBCheck.getFlashPlayerVersion();
  var meetsRequirement = BBBCheck.hasMinFlashPlayerVersion(minVersion);
  callback(playerVersion.major + '.' + playerVersion.minor + '.' + playerVersion.release, meetsRequirement);
}

function bbbCheckJavaVersion(versionPattern, callback) {
  var installedVersions = BBBCheck.getJREs().join(', ');
  var meetsRequirement = BBBCheck.JREVersionCheck(versionPattern);
  callback(installedVersions, meetsRequirement);
}

var bbbCheckShowMicSettings = function() {
  BBBCheck.showMicSettings();
}

var bbbCheckStartTestMic = function() {
  BBBCheck.startTestMicrophone();
}

var bbbCheckStopTestMic = function() {
  BBBCheck.stopTestMicrophone();
}

var bbbCheckShowCamSettings = function() {
  BBBCheck.showCamSettings();
}

var bbbCheckTestRTMPConnection = function(host, app) {
  BBBCheck.testRTMPConnection(host, app);
}

var bbbCheckTestSocketConnection = function(host, port) {
  BBBCheck.testSocketConnection(host, port);
}

var bbbCheckSetSocketPolicyFileURL = function(url) {
  BBBCheck.setSocketPolicyFileURL(url);
}
