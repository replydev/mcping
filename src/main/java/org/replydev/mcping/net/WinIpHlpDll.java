package me.replydev.mcping.net;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static java.lang.reflect.Modifier.isStatic;

/**
 * JNA binding for iphlpapi.dll for ICMP and ARP support under Windows
 */
public interface WinIpHlpDll extends Library {
    WinIpHlpDll dll = Loader.load();

    /**
     * Wrapper for Microsoft's <a href="http://msdn.microsoft.com/en-US/library/aa366045.aspx">IcmpCreateFile</a>
     */
    Pointer IcmpCreateFile();

    /**
     * Wrapper for Microsoft's <a href="https://docs.microsoft.com/en-us/windows/desktop/api/icmpapi/nf-icmpapi-icmp6createfile">Icmp6CreateFile</a>
     */
    Pointer Icmp6CreateFile();

    /**
     * Wrapper for Microsoft's <a href="http://msdn.microsoft.com/en-us/library/aa366043.aspx">IcmpCloseHandle</a>
     */
    void IcmpCloseHandle(Pointer hIcmp);

    /**
     * Wrapper for Microsoft's <a href="http://msdn.microsoft.com/EN-US/library/aa366050.aspx">IcmpSendEcho</a>
     */
    int IcmpSendEcho(
            Pointer hIcmp,
            IpAddrByVal destinationAddress,
            Pointer requestData,
            short requestSize,
            IpOptionInformationByRef requestOptions,
            Pointer replyBuffer,
            int replySize,
            int timeout
    );

    /**
     * Wrapper for Microsoft's <a href="https://docs.microsoft.com/en-us/windows/desktop/api/icmpapi/nf-icmpapi-icmp6sendecho2">Icmp6SendEcho2</a>
     */
    int Icmp6SendEcho2(
            Pointer hIcmp,
            Pointer event,
            Pointer apcRoutine,
            Pointer apcContext,
            Ip6SockAddrByRef sourceAddress,
            Ip6SockAddrByRef destinationAddress,
            Pointer requestData,
            short requestSize,
            IpOptionInformationByRef requestOptions,
            Pointer replyBuffer,
            int replySize,
            int timeout
    );

    /**
     * Wrapper for Microsoft's <a href="http://msdn.microsoft.com/en-us/library/windows/desktop/aa366358(v=vs.85).aspx">SendARP</a>
     */
    int SendARP(
            IpAddrByVal destIP,
            int srcIP,
            Pointer pMacAddr,
            Pointer pPhyAddrLen
    );

    class Loader {
        static WinIpHlpDll load() {
            try {
                return Native.load("iphlpapi", WinIpHlpDll.class);
            } catch (UnsatisfiedLinkError e) {
                return Native.load("icmp", WinIpHlpDll.class);
            }
        }
    }

    class AutoOrderedStructure extends Structure {
        // this is a requirement of newer JNA, possibly it won't work on some JVM, but probability is quite small
        @Override
        protected List<String> getFieldOrder() {
            ArrayList<String> fields = new ArrayList<>();
            for (Field field : getClass().getFields()) {
                if (!isStatic(field.getModifiers()))
                    fields.add(field.getName());
            }
            return fields;
        }
    }

    class IpAddr extends AutoOrderedStructure {
        public byte[] bytes = new byte[4];
    }

    class IpAddrByVal extends IpAddr implements Structure.ByValue {
    }

    class Ip6SockAddr extends AutoOrderedStructure {
        public short family = 10;
        public short port;
        public int flowInfo;
        public byte[] bytes = new byte[16];
        public int scopeId;
    }

    class Ip6SockAddrByRef extends Ip6SockAddr implements Structure.ByReference {
    }

    class IpOptionInformation extends AutoOrderedStructure {
        public byte ttl;
        public byte tos;
        public byte flags;
        public byte optionsSize;
        public Pointer optionsData;
    }

    class IpOptionInformationByVal
            extends IpOptionInformation implements Structure.ByValue {
    }

    class IpOptionInformationByRef
            extends IpOptionInformation implements Structure.ByReference {
    }

    class IcmpEchoReply extends AutoOrderedStructure {
        public IpAddrByVal address;
        public int status;
        public int roundTripTime;
        public short dataSize;
        public short reserved;
        public Pointer data;
        public IpOptionInformationByVal options;

        public IcmpEchoReply() {
        }

        public IcmpEchoReply(Pointer p) {
            useMemory(p);
            read();
        }
    }

    class Icmp6EchoReply extends AutoOrderedStructure {
        public final byte[] addressBytes = new byte[16];
        public short port;
        public byte[] flowInfo = new byte[4];
        public int scopeId;
        public int status;
        public int roundTripTime;

        public Icmp6EchoReply() {
        }

        public Icmp6EchoReply(Pointer p) {
            useMemory(p);
            read();
        }
    }
}
