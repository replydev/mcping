package org.replydev.mcping;

import lombok.Builder;
import lombok.Value;
import org.replydev.mcping.model.ServerResponse;
import org.replydev.mcping.utils.Singleton;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Minecraft Service List Protocol Pinger
 * following 1.7+ protocol.
 *
 */
@Value
@Builder
public class MCPinger {

    PingOptions pingOptions;

    private int readVarInt(DataInputStream in) throws IOException {
        int i = 0;
        int j = 0;
        int k;
        do {
            k = in.readByte();
            i |= (k & 0x7F) << j++ * 7;
            if (j > 5) {
                //throw new RuntimeException("VarInt too big");
                return -1;
            }
        } while ((k & 0x80) == 128);
        return i;
    }

    private void writeVarInt(DataOutputStream out, int paramInt) throws IOException {
        while(true) {
            if ((paramInt & 0xFFFFFF80) == 0) {
                out.writeByte(paramInt);
                return;
            }
            out.writeByte(paramInt & 0x7F | 0x80);
            paramInt >>>= 7;
        }
    }

    public ServerResponse fetchData() throws IOException {
        InetSocketAddress socketAddress = pingOptions.getSocketAddress();
        Socket socket = new Socket();
        socket.setSoTimeout(pingOptions.getTimeout());
        socket.connect(socketAddress, pingOptions.getTimeout());

        OutputStream outputStream = socket.getOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
        InputStream inputStream = socket.getInputStream();
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream handshake = new DataOutputStream(b);

        handshake.writeByte(0);
        writeVarInt(handshake, pingOptions.getProtocolVersion());
        writeVarInt(handshake, socketAddress.getHostString().length());
        handshake.writeBytes(socketAddress.getHostString());
        handshake.writeShort(socketAddress.getPort());
        writeVarInt(handshake, 1);

        writeVarInt(dataOutputStream, b.size());
        dataOutputStream.write(b.toByteArray());
        dataOutputStream.writeByte(1);
        dataOutputStream.writeByte(0);
        DataInputStream dataInputStream = new DataInputStream(inputStream);

        int size = readVarInt(dataInputStream);
        int id = readVarInt(dataInputStream);
        int length = readVarInt(dataInputStream);

        if (size < 0 || id < 0 || length <= 0) {
            closeAll(b, dataInputStream, handshake, dataOutputStream, outputStream, inputStream, socket);
            throw new IOException("Size or id or length is malformed");
        }

        byte[] fetchedData = new byte[length];
        dataInputStream.readFully(fetchedData);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(fetchedData);
        ServerResponse response = Singleton.dslJson.deserialize(ServerResponse.class,byteArrayInputStream);
        closeAll(b, dataInputStream, handshake, dataOutputStream, outputStream, inputStream, socket);
        return response;
    }

    private void closeAll(Closeable... closeables) throws IOException {
        for (Closeable closeable : closeables) {
            closeable.close();
        }
    }
}
