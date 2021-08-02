package ru.kolevatykh.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.spark.api.java.StorageLevels;
import org.apache.spark.streaming.receiver.Receiver;
import org.pcap4j.core.BpfProgram;
import org.pcap4j.core.PacketListener;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.packet.Packet;
import ru.kolevatykh.config.PcapConfig;

@Slf4j
public class PcapReceiver extends Receiver<Integer> {

    private final String ip;
    private final Integer deviceId;

    public PcapReceiver(String ip, Integer deviceId) {
        super(StorageLevels.MEMORY_AND_DISK_SER);
        this.ip = ip;
        this.deviceId = deviceId;
    }

    @Override
    public void onStart() {
        try {
            handlePacket();
        } catch (Exception e) {
            log.debug(e.getMessage());
        }
    }

    @Override
    public void onStop() {

    }

    public void handlePacket() throws Exception {
        PcapHandle handle = PcapConfig.pcapHandle(deviceId);

        if (ip != null) {
            handle.setFilter("host " + ip, BpfProgram.BpfCompileMode.OPTIMIZE);
        }

        PacketListener listener = new PacketListener() {
            @Override
            public void gotPacket(Packet packet) {
                log.info("Packet raw: {}", packet.getPayload());
                store(packet.length());
            }
        };

        handle.loop(-1, listener);
        handle.close();
    }
}
