package com.barclays.indiacp.service.task;

import com.barclays.indiacp.error.APIException;
import com.barclays.indiacp.model.Peer;
import com.barclays.indiacp.service.NodeService;
import com.barclays.indiacp.dao.PeerDAO;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Load stored peers and try to reconnect to them
 *
 * @author Chetan Sarva
 *
 */
@Component
@Scope("prototype")
public class LoadPeersTask implements Runnable {

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory
            .getLogger(LoadPeersTask.class);

    @Autowired
    private PeerDAO peerDAO;

    @Autowired
    private NodeService nodeService;

    @Override
    public void run() {

        List<Peer> peers = peerDAO.list();
        if (peers.size() > 0) {
            LOG.info("Reconnecting " + peers.size() + " peer(s)");
        }

        for (Peer peer : peers) {
            try {
                LOG.debug("Reconnecting to " + peer.getNodeUrl());
                nodeService.addPeer(peer.getNodeUrl());
            } catch (APIException e) {
                LOG.warn("Failed to add peer " + peer.getNodeUrl());
            }
        }

    }

}
