package me.besser.teawutils.opac;

import me.besser.teawutils.util.TEAWUtilsConfig;
import net.minecraft.network.chat.Component;

import xaero.pac.common.claims.action.api.ClaimingAction;
import xaero.pac.common.claims.api.SpecialClaimOwners;
import xaero.pac.common.server.api.OpenPACServerAPI;
import xaero.pac.common.server.claims.action.listener.api.IClaimActionListenerAPI;
import xaero.pac.common.server.claims.action.listener.override.api.ClaimActionPermissionOverride;
import xaero.pac.common.server.claims.action.listener.override.api.ClaimActionPermissionOverrideType;

public class PartyClaimListener implements IClaimActionListenerAPI {

    private static boolean enableRequirePartyClaims = true;

    @Override
    public String getName() {
        return "TEAW Utils Party Claim Listener";
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

        // Only affect new claims
        if (action != ClaimingAction.CLAIM) {
            return currentOverride;
        }

        // Allow server claims
        if (playerId.equals(SpecialClaimOwners.SERVER)) {
            return currentOverride;
        }

        var partyManager = OpenPACServerAPI.get(server).getPartyManager();

        // Claims being made as a party use the party owner's UUID.
        // This works even when the party owner is offline.
        if (partyManager.getPartyByOwner(playerId) != null) {
            return currentOverride;
        }

        // The player belongs to a party but is claiming individually
        if (partyManager.getPartyByMember(playerId) != null) {
            return deny(Component.translatable(
                    "teawutils.opac.claim.party_mode_required"
            ));
        }

        // The player doesn't belong to a party
        return deny(Component.translatable(
                "teawutils.opac.claim.no_party"
        ));
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
    ) {}

    private ClaimActionPermissionOverride deny(Component message) {
        return new ClaimActionPermissionOverride(
                ClaimActionPermissionOverrideType.INTERRUPT,
                message
        );
    }

    public static void reloadConfig() {
        enableRequirePartyClaims =
                TEAWUtilsConfig.COMMON.requirePartyToClaim.get();
    }
}