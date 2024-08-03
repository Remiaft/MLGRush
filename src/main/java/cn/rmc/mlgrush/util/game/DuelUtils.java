package cn.rmc.mlgrush.util.game;

import cn.rmc.mlgrush.Language;
import cn.rmc.mlgrush.MLGRush;
import cn.rmc.mlgrush.data.Game;
import cn.rmc.mlgrush.data.PlayerData;
import cn.rmc.mlgrush.enums.Mode;
import cn.rmc.mlgrush.enums.State;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.codec.language.bm.Lang;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class DuelUtils {
    public static HashMap<Player, Player> request = new HashMap<>();
    public static HashMap<Player, Long> timer = new HashMap<>();
    public static void Request(Player requester,Player receiver){
        PlayerData requests = MLGRush.getInstance().getPlayerManager().get(requester);
        if(timer.get(requester) != null) {
            if (System.currentTimeMillis() - timer.get(requester) < 5000) {
                requester.sendMessage(Language.get().translate("Message.duel.toooften"));
                return;
            }
            if (request.get(requester) == receiver && System.currentTimeMillis() - timer.get(requester) < 15000) {
                requester.sendMessage(Language.get().translate("Message.duel.already"));
                return;
            }
        }
        request.put(requester,receiver);
        timer.put(requester,System.currentTimeMillis());
        requester.sendMessage(Language.get().translate("Message.duel.sent").replace("%opponent%",receiver.getName()));//("已发送一条对战请求给 "+receiver.getName());
        TextComponent option = new TextComponent();
        option = new TextComponent(Language.get().translate("Message.duel.clicker"));
        option.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[]{new TextComponent(Language.get().translate("Message.duel.clicker_show"))}));
        option.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/duel accept "+requester.getName()));
        receiver.sendMessage(Language.get().translate("Message.duel.received_1").replace("%requester%",requester.getName()));
        receiver.sendMessage(Language.get().translate("Message.duel.received_2").replace("%requester%",requester.getName()));
        receiver.spigot().sendMessage(option);
    }
    public static void Accept(Player accpeter,Player requester){
        PlayerData accept = MLGRush.getInstance().getPlayerManager().get(accpeter);
        if(requester == null){
            accpeter.sendMessage(Language.get().translate("Message.duel.opponentoffline"));
            return;
        }
        PlayerData requests = MLGRush.getInstance().getPlayerManager().get(requester);
        if(request.get(requester) != accpeter){
            accpeter.sendMessage(Language.get().translate("Message.duel.noquest"));
            return;
        }
        if(System.currentTimeMillis() - timer.get(requester) > 15000L){
            accpeter.sendMessage(Language.get().translate("Message.duel.requestexpired"));
            return;
        }
        if(requests.getState() == State.QUEUE) MLGRush.getInstance().getQueueManager().remove(requester);
        if(accept.getState() == State.QUEUE) MLGRush.getInstance().getQueueManager().remove(accpeter);
        new Game(requests,accept,MLGRush.getInstance().getMapManager().getMap(), Mode.DUEL);

    }
}
