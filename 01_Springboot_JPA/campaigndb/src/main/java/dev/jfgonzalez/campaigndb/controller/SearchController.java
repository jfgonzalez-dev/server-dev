package dev.jfgonzalez.campaigndb.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import dev.jfgonzalez.campaigndb.model.Campaign;
import dev.jfgonzalez.campaigndb.model.GameCharacter;
import dev.jfgonzalez.campaigndb.model.Player;
import dev.jfgonzalez.campaigndb.repository.CampaignRepository;
import dev.jfgonzalez.campaigndb.repository.GameCharacterRepository;
import dev.jfgonzalez.campaigndb.repository.PlayerRepository;


@Controller 
@RequestMapping("/search")
public class SearchController {
    private static final String CAMPAIGNS_ATTR = "campaigns";
    private static final String GAME_CHARACTERS_ATTR = "gameCharacters";

    private final CampaignRepository campaignRepository;
    private final GameCharacterRepository gameCharacterRepository;
    private final PlayerRepository playerRepository;
    
    public SearchController (
        CampaignRepository campaignRepository,
        GameCharacterRepository gameCharacterRepository,
        PlayerRepository playerRepository
    ) {
        this.campaignRepository = campaignRepository;
        this.gameCharacterRepository = gameCharacterRepository;
        this.playerRepository = playerRepository;
    }

    @GetMapping
    public String searchByName(
        @RequestParam(defaultValue = "campaign") String entity,
        @RequestParam(defaultValue = "0") int pageNumber,
        @RequestParam(defaultValue = "10") int pageSize, 
        @RequestParam(defaultValue = "") String name,
        @RequestParam(defaultValue = "") String gameSystem,
        @RequestParam(defaultValue = "") String status,
        @RequestParam(required = false) UUID ownerId,
        @RequestParam(required = false) UUID campaignId,
        @RequestParam(defaultValue = "false") boolean pcOnly,
        @RequestParam(defaultValue = "") String comparator,
        @RequestParam(defaultValue = "-1") Integer level,
        Model model
    ) {
        if (entity.equals("campaign")) {
            searchCampaigns(pageNumber, pageSize, model, name, gameSystem, status, ownerId);

            
        } else if (entity.equals("gameCharacter")) {
            Page<GameCharacter> gameCharacters = gameCharacterRepository.findByNameContainsIgnoreCase(name, PageRequest.of(pageNumber, pageSize));    
            model.addAttribute(GAME_CHARACTERS_ATTR,gameCharacters);
        }
        model.addAttribute("allPlayers", playerRepository.findAll());
        model.addAttribute("allCampaigns", campaignRepository.findAll());
        model.addAttribute("gameSystems", fetchGameSystems());
        
        model.addAttribute("entity", entity);
        return "search";
    }

    private List<String> fetchGameSystems() {
        return campaignRepository.findAll().stream().map(c -> c.getGameSystem()).distinct().toList();
    }

    private <T> Page<T> toPage(List<T> list, int pageNumber, int pageSize) {
        int start = Math.min(pageNumber * pageSize, list.size());
        int end = Math.min(start + pageSize, list.size());
        return new PageImpl<>(list.subList(start, end), PageRequest.of(pageNumber, pageSize), list.size());
    }

    private List<Campaign> filterByName(List<Campaign> list, String name) {
        return list.stream().filter(c -> c.getName() != null && c.getName().toLowerCase().contains(name.toLowerCase())).toList();
    }
    
    private List<Campaign> filterByGameSystem(List<Campaign> list, String gameSystem) {
        return list.stream().filter(c -> c.getGameSystem().equalsIgnoreCase(gameSystem)).toList();
    }

    private void searchCampaigns(int pageNumber, int pageSize, Model model, String name, String gameSystem, String status, UUID ownerId) {
        List<Campaign> campaigns;
        if (ownerId != null) { // 0001
            campaigns = campaignRepository.findByOwnerIdOrderByStatus(ownerId); // 0001
            if (!status.isBlank()) { // 0011
                Player owner = playerRepository.findById(ownerId).orElseThrow();
                campaigns = campaignRepository.findByOwnerAndStatusContainsIgnoreCase(owner, status); // 0011
                if (!gameSystem.isBlank()) { // 0111
                    campaigns = filterByGameSystem(campaigns, gameSystem); // 0111
                    if (!name.isBlank()) { // 1111
                        campaigns = filterByName(campaigns, name); // 1111
                    }
                } else {
                    if (!name.isBlank()) { // 1011
                        campaigns = filterByName(campaigns, name); // 1011
                    }
                }
            } else {
                if (!gameSystem.isBlank()) { // 0101
                    campaigns = filterByGameSystem(campaigns, gameSystem); // 0101
                    if (!name.isBlank()) { // 1101
                        campaigns = filterByName(campaigns, name); // 1101
                    }
                } else { // 0001
                    if (!name.isBlank()) { // 1001
                        campaigns = filterByName(campaigns, name);
                    }
                }
            }

        } else {
            if (!status.isBlank()) { // 0010
                campaigns = campaignRepository.findByStatusIgnoreCase(status); // 0010
                if (!gameSystem.isBlank()) { // 0110
                    campaigns = campaignRepository.findByGameSystemIgnoreCaseAndStatusContainsIgnoreCase(gameSystem, status); // 0110
                    if (!name.isBlank()) { // 1110
                        campaigns = filterByName(campaigns, name); // 1110
                    }
                } else {
                    if (!name.isBlank()) { // 1010
                        campaigns = filterByName(campaigns, name); // 1010
                    }
                }
            } else {
                if (!gameSystem.isBlank()) { // 0100
                    campaigns = filterByGameSystem(campaignRepository.findAll(), gameSystem); // 0100
                    if (!name.isBlank()) { // 1100
                        campaigns = filterByGameSystem(campaignRepository.findByNameContainsIgnoreCase(name), gameSystem);
                    }
                } else {
                    if (!name.isBlank()) { // 1000
                        campaigns = campaignRepository.findByNameContainsIgnoreCase(name);
                    } else {
                        campaigns = campaignRepository.findAll(); // 0000
                    }
                }
            }

        }

        model.addAttribute(CAMPAIGNS_ATTR, toPage(campaigns, pageNumber, pageSize));
    }    
}
