DESCRIPTION = "ifplugd is a Linux daemon which will automatically configure your ethernet device \
when a cable is plugged in and automatically unconfigure it if the cable is pulled."
HOMEPAGE = "http://0pointer.de/lennart/projects/ifplugd/"
SECTION = "network"
DEPENDS = "libdaemon"
LICENSE = "CLOSED"
PR = "r1"

SRC_URI = "http://0pointer.de/lennart/projects/ifplugd/ifplugd-${PV}.tar.gz \
 file://kernel-types.patch \
 file://nobash.patch \
 file://ocsifplugd.conf"

inherit autotools update-rc.d pkgconfig

EXTRA_OECONF = "--disable-lynx"


INITSCRIPT_NAME = "ifplugd"
INITSCRIPT_PARAMS = "start 20 . stop 20 ."

CONFFILES_${PN} = "${sysconfdir}/ifplugd/ifplugd.conf"

SRC_URI[md5sum] = "cbb45e24684fe5ba7a60730248cf250b"
SRC_URI[sha256sum] = "a43c0621dac846e42a3917f4f73e7976b2ac4b545712e8bc4bae5bac6158e07e"

do_install_append() {
install -d -m 0766 ${D}${sysconfdir}/ifplugd/
install -m 644 ${WORKDIR}/ocsifplugd.conf ${D}${sysconfdir}/ifplugd/ifplugd.conf
}