SUMMARY = "An implementation of the standard Unix documentation system accessed using the man command"
HOMEPAGE = "http://man-db.nongnu.org/"
LICENSE = "LGPLv2.1 & GPLv2"
LIC_FILES_CHKSUM = "file://docs/COPYING.LIB;md5=a6f89e2100d9b6cdffcea4f398e37343 \
                    file://docs/COPYING;md5=eb723b61539feef013de476e68b5c50a"

SRC_URI = "${SAVANNAH_NONGNU_MIRROR}/man-db/man-db-${PV}.tar.xz \
           file://99_mandb \
           file://man_db.conf-avoid-multilib-install-file-conflict.patch"
SRC_URI[md5sum] = "c5c6c3434be14a5527d43b5ad0f09a13"
SRC_URI[sha256sum] = "b64d52747534f1fe873b2876eb7f01319985309d5d7da319d2bc52ba1e73f6c1"

DEPENDS = "libpipeline gdbm groff-native base-passwd"
RDEPENDS_${PN} += "base-passwd"

# | /usr/src/debug/man-db/2.8.0-r0/man-db-2.8.0/src/whatis.c:939: undefined reference to `_nl_msg_cat_cntr'
USE_NLS_libc-musl = "no"

inherit gettext pkgconfig autotools systemd

EXTRA_OECONF = "--with-pager=less"
EXTRA_AUTORECONF += "-I ${S}/gl/m4"

do_install() {
	autotools_do_install

	if ${@bb.utils.contains('DISTRO_FEATURES', 'sysvinit', 'true', 'false', d)}; then
	        install -d ${D}/etc/default/volatiles
		install -m 0644 ${WORKDIR}/99_mandb ${D}/etc/default/volatiles
	fi
}

do_install_append_libc-musl() {
        rm -f ${D}${libdir}/charset.alias
}

FILES_${PN} += "${prefix}/lib/tmpfiles.d"

FILES_${PN}-dev += "${libdir}/man-db/libman.so ${libdir}/${BPN}/libmandb.so"

RDEPENDS_${PN} += "groff"
RRECOMMENDS_${PN} += "less"
RPROVIDES_${PN} += " man"

def compress_pkg(d):
    if bb.utils.contains("INHERIT", "compress_doc", True, False, d):
         compress = d.getVar("DOC_COMPRESS")
         if compress == "gz":
             return "gzip"
         elif compress == "bz2":
             return "bzip2"
         elif compress == "xz":
             return "xz"
    return ""

RDEPENDS_${PN} += "${@compress_pkg(d)}"

SYSTEMD_SERVICE_${PN} = "man-db.timer man-db.service"
SYSTEMD_AUTO_ENABLE ?= "disable"
