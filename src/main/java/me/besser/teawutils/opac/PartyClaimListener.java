package me.besser.teawutils.opac;


import me.besser.teawutils.util.TEAWUtilsConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import xaero.pac.common.claims.action.api.ClaimingAction;
import xaero.pac.common.claims.api.SpecialClaimOwners;
import xaero.pac.common.claims.player.mode.api.ClaimingModes;
import xaero.pac.common.server.api.OpenPACServerAPI;
import xaero.pac.common.server.claims.action.listener.api.IClaimActionListenerAPI;
import xaero.pac.common.server.claims.action.listener.override.api.ClaimActionPermissionOverride;
import xaero.pac.common.server.claims.action.listener.override.api.ClaimActionPermissionOverrideType;
import xaero.pac.common.server.player.data.api.ServerPlayerDataAPI;

public class PartyClaimListener implements IClaimActionListenerAPI {

    private static boolean enableRequirePartyClaims = true;

    @Override
    public String getName() {
        return "TEAW Utils Party Claim Enforcement";
    }

    @Override
    public ClaimActionPermissionOverride overrideClaimingActionPermission(
            java.util.UUID playerId,
            net.minecraft.resources.ResourceLocation dim,
            int x,
            int z,
            ClaimingAction action,
            xaero.pac.common.server.claims.api.IServerClaimsManagerAPI claimsManagerAPI,
            ClaimActionPermissionOverride currentOverride,
            net.minecraft.server.MinecraftServer server
    ) {
        if (!enableRequirePartyClaims) {
            return currentOverride;
        }

        // Only enforce this rule when claiming new land.
        // Unclaiming and forceloading are unaffected.
        if (action != ClaimingAction.CLAIM) {
            return currentOverride;
        }

        if (playerId.equals(SpecialClaimOwners.SERVER)) {
            return currentOverride;
        }

        // Check whether the player belongs to a party.
        boolean isInParty = OpenPACServerAPI.get(server)
                .getPartyManager()
                .getPartyByMember(playerId) != null;

        // TODO: put strings in lang file
        if (!isInParty) {
            return deny(
                    "You must join a party before claiming land!"
            );
        }

        // Get the player so we can check their claiming mode
        // TODO: when the party owner is offline, the could not verify mode will show up.
        // TODO: When the party owner is in the player claim mode (not party) the wrong mode error will show up.
        ServerPlayer player = server.getPlayerList().getPlayer(playerId);

        if (player == null) {
            return deny(
                    "Could not verify your claiming mode. Please try again."
            );
        }

        // Party members must claim as their party, never individually
        if (!ServerPlayerDataAPI.from(player)
                .getClaimingMode()
                .equals(ClaimingModes.PARTY)) {

            return deny(
                    "You are in a party! Switch your Claiming Mode to "
                            + "'Claiming as Party' before claiming land."
            );
        }

        return currentOverride;
    }

    private ClaimActionPermissionOverride deny(String message) {
        return new ClaimActionPermissionOverride(
                ClaimActionPermissionOverrideType.INTERRUPT,
                Component.literal(message)
        );
    }

    @Override
    public void handleSuccessfulClaimingAction(
            java.util.UUID playerId,
            net.minecraft.resources.ResourceLocation dim,
            int x,
            int z,
            ClaimingAction action,
            xaero.pac.common.server.claims.api.IServerClaimsManagerAPI claimsManagerAPI,
            net.minecraft.server.MinecraftServer server
    ) {
        // Nothing needed
    }


    public static void reloadConfig() {
        enableRequirePartyClaims = TEAWUtilsConfig.COMMON.requirePartyToClaim.get();
    }
}
