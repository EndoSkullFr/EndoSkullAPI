package fr.endoskull.api.spigot.papi;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.provider.ServiceTaskProvider;
import de.dytanic.cloudnet.driver.service.ServiceInfoSnapshot;
import de.dytanic.cloudnet.ext.bridge.ServiceInfoSnapshotUtil;
import de.dytanic.cloudnet.ext.bridge.player.IPlayerManager;
import de.dytanic.cloudnet.wrapper.Wrapper;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;

public class CloudNetExpansion extends PlaceholderExpansion {
    private final String VERSION = getClass().getPackage().getImplementationVersion();

    private Wrapper cloudnet = Wrapper.getInstance();

    private ServiceTaskProvider task = this.cloudnet.getServiceTaskProvider();

    public boolean canRegister() {
        return true;
    }

    public String getIdentifier() {
        return "cloudnet";
    }

    public String getAuthor() {
        return "ShadowChildUK";
    }

    public String getVersion() {
        return "1.0.0";
    }

    public String onRequest(OfflinePlayer player, String rawParams) {
        ServiceInfoSnapshot serviceInfoSnapshot = this.cloudnet.getCurrentServiceInfoSnapshot();
        String params = rawParams.toLowerCase();
        IPlayerManager players = (IPlayerManager)CloudNetDriver.getInstance().getServicesRegistry().getFirstService(IPlayerManager.class);
        if (params.startsWith("playercount_")) {
            String[] args = params.split("playercount_");
            if (args.length < 2)
                return null;
            return String.valueOf(ServiceInfoSnapshotUtil.getTaskOnlineCount(args[1]));
        }
        switch (params) {
            case "service_name":
                return serviceInfoSnapshot.getServiceId().getName();
            case "service_static":
                return "" + serviceInfoSnapshot.getConfiguration().isStaticService();
            case "service_autodelete":
                return "" + serviceInfoSnapshot.getConfiguration().isAutoDeleteOnStop();
            case "service_port":
                return "" + serviceInfoSnapshot.getAddress().getPort();
            case "service_host":
                return "" + serviceInfoSnapshot.getAddress().getHost();
            case "service_address":
                return serviceInfoSnapshot.getAddress().getHost() + ":" + serviceInfoSnapshot.getAddress().getPort();
            case "task_name":
                return serviceInfoSnapshot.getServiceId().getTaskName();
            case "total_online":
                return "" + players.getOnlineCount();
            case "service_number":
                return "" + serviceInfoSnapshot.getServiceId().getTaskServiceId();
        }
        return null;
    }
}