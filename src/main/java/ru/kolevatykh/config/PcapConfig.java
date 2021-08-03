package ru.kolevatykh.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.core.Pcaps;

import java.util.List;

@Slf4j
@Data
public class PcapConfig {

    private static final int snapshotLength = 65536;
    private static final int readTimeout = 500;
    private static final List<PcapNetworkInterface> pcapList = getNetworkDevices();

    public static PcapHandle pcapHandle(Integer deviceId) {
        PcapHandle handle = null;
        try {
            handle = pcapList.get(deviceId)
                    .openLive(snapshotLength, PcapNetworkInterface.PromiscuousMode.PROMISCUOUS, readTimeout);
        } catch (PcapNativeException e) {
            log.debug(e.getMessage());
        }
        return handle;
    }

    public static List<PcapNetworkInterface> getNetworkDevices() {
        List<PcapNetworkInterface> devices = null;
        try {
            devices = Pcaps.findAllDevs();
            if (devices == null || devices.isEmpty()) {
                throw new Exception("Devices are not found.");
            }
        } catch (Exception e) {
            log.debug(e.getMessage());
        }
        return devices;
    }
}
