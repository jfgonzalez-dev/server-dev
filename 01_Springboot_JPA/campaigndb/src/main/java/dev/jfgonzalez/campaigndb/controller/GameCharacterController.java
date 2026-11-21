package dev.jfgonzalez.campaigndb.controller;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import dev.jfgonzalez.campaigndb.dto.GameCharacterDTO;
import dev.jfgonzalez.campaigndb.model.Campaign;
import dev.jfgonzalez.campaigndb.model.GameCharacter;
import dev.jfgonzalez.campaigndb.model.Player;
import dev.jfgonzalez.campaigndb.repository.CampaignRepository;
import dev.jfgonzalez.campaigndb.repository.GameCharacterRepository;
import dev.jfgonzalez.campaigndb.repository.PlayerRepository;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PostMapping;


@Controller 
@RequestMapping("/gameCharacter")
public class GameCharacterController {

    private static final String GAME_CHARACTER_ATTR = "gameCharacter";
    private static final String CAMPAIGNS_ATTR = "campaigns";

    @SuppressWarnings("java:S1075")
    private static final String GAME_CHARACTER_PATH = "redirect:/gameCharacter";
    private static final String DETAILS_PATH = "/details";

    private final GameCharacterRepository gameCharacterRepository;
    private final CampaignRepository campaignRepository;
    private final PlayerRepository playerRepository;

    public GameCharacterController(
        GameCharacterRepository gameCharacterRepository,
        CampaignRepository campaignRepository,
        PlayerRepository playerRepository
    ){
        this.gameCharacterRepository = gameCharacterRepository;
        this.campaignRepository = campaignRepository;
        this.playerRepository = playerRepository;
    }

    @GetMapping
    public String listGameCharacters(
        @RequestParam(defaultValue = "0") int pageNumber,
        @RequestParam(defaultValue = "10") int pageSize,
        Model model
    ) {
        Page<GameCharacter> gameCharacters = gameCharacterRepository.findAll(PageRequest.of(pageNumber, pageSize));
        if (pageNumber > 0 && gameCharacters.isEmpty() && gameCharacters.getTotalPages() > 0) {
            return GAME_CHARACTER_PATH + "?pageNumber=" + (gameCharacters.getTotalPages() - 1) + "&pageSize=" + pageSize;
        }
        model.addAttribute("gameCharacters", gameCharacters);
        return "gameCharacter/list";
    }

    @GetMapping("/new")
    public String newGameCharacter(
        @RequestParam(required = false) UUID campaignId,
        @RequestParam(required = false) UUID ownerId,
        Model model
    ) {
        GameCharacterDTO dto = new GameCharacterDTO(
            null, "", "", true, 1,
            "", campaignId, ownerId
        );
        model.addAttribute(GAME_CHARACTER_ATTR, dto);
        List<Campaign> campaigns = campaignRepository.findAll();
        model.addAttribute(CAMPAIGNS_ATTR, campaigns);
        List<Player> players = playerRepository.findAll();
        model.addAttribute("players", players);
        return "gameCharacter/new";
    }

    @PostMapping("/create")
    public String createGameCharacter(@ModelAttribute GameCharacterDTO dto) {
        GameCharacter gameCharacter = new GameCharacter();
        gameCharacter.setName(dto.name());
        gameCharacter.setBackground(dto.background());
        gameCharacter.setPc(dto.pc());
        gameCharacter.setLevel(dto.level());
        gameCharacter.setAvatarUrl(dto.avatarUrl() != null ? dto.avatarUrl().trim() : "");
        gameCharacter.setCampaign(dto.campaignId() != null ? campaignRepository.findById(dto.campaignId()).orElseThrow() : null);
        gameCharacter.setOwner(dto.ownerId() != null ? playerRepository.findById(dto.ownerId()).orElseThrow() : null);
        gameCharacter.setProperties(new HashMap<>());
        gameCharacterRepository.save(gameCharacter);
        return GAME_CHARACTER_PATH;
    }

    @GetMapping("/{id}/edit")
    public String editGameCharacter(@PathVariable UUID id, Model model) {
        GameCharacter gameCharacter = gameCharacterRepository.findById(id).orElseThrow();
        GameCharacterDTO dto = new GameCharacterDTO(
            gameCharacter.getId(),
            gameCharacter.getName(),
            gameCharacter.getBackground(),
            gameCharacter.isPc(),
            gameCharacter.getLevel(),
            gameCharacter.getAvatarUrl(),
            gameCharacter.getCampaign() != null ? gameCharacter.getCampaign().getId() : null,
            gameCharacter.getOwner() != null ? gameCharacter.getOwner().getId() : null
        );
        model.addAttribute(GAME_CHARACTER_ATTR, dto);
        List<Campaign> campaigns = campaignRepository.findAll();
        model.addAttribute(CAMPAIGNS_ATTR, campaigns);
        List<Player> players = playerRepository.findAll();
        model.addAttribute("players", players);
        return "gameCharacter/edit";
    }

    @PostMapping("/modify")
    public String modifyGameCharacter(@ModelAttribute GameCharacterDTO dto) {
        GameCharacter gameCharacter = gameCharacterRepository.findById(dto.id()).orElseThrow();
        gameCharacter.setName(dto.name());
        gameCharacter.setBackground(dto.background());
        gameCharacter.setPc(dto.pc());
        gameCharacter.setLevel(dto.level());
        gameCharacter.setAvatarUrl(dto.avatarUrl() != null ? dto.avatarUrl().trim() : "");
        gameCharacter.setCampaign(dto.campaignId() != null ? campaignRepository.findById(dto.campaignId()).orElseThrow() : null);
        gameCharacter.setOwner(dto.ownerId() != null ? playerRepository.findById(dto.ownerId()).orElseThrow() : null);
        gameCharacterRepository.save(gameCharacter);        
        return GAME_CHARACTER_PATH + "/" + dto.id() + DETAILS_PATH;
    }
    
    @GetMapping("/{id}/delete")
    @Transactional 
    public String deleteGameCharacter(
        @PathVariable UUID id,
        @RequestParam(defaultValue = "0") int pageNumber,
        @RequestParam(defaultValue = "10") int pageSize,
        RedirectAttributes redirectAttributes
    ) {
        gameCharacterRepository.deleteById(id);
        redirectAttributes.addAttribute("pageNumber", pageNumber);
        redirectAttributes.addAttribute("pageSize", pageSize);
        return GAME_CHARACTER_PATH;
    }

    @GetMapping("/{id}/details")
    public String detailsGameCharacter(@PathVariable UUID id, Model model) {
        GameCharacter gameCharacter = gameCharacterRepository.findById(id).orElseThrow();
        model.addAttribute(GAME_CHARACTER_ATTR, gameCharacter);
        List<Campaign> campaigns = campaignRepository.findAll();
        model.addAttribute(CAMPAIGNS_ATTR, campaigns);
        return "gameCharacter/details";
    }

    @GetMapping("/{id}/deassignCampaign")
    public String deassignCampaign(
        @PathVariable UUID id,
        @RequestParam(defaultValue = "campaign") String redirectTo
    ) {
        GameCharacter gameCharacter = gameCharacterRepository.findById(id).orElseThrow();
        UUID campaignId = null;
        if (gameCharacter.getCampaign() != null) {
            campaignId = gameCharacter.getCampaign().getId();
            gameCharacter.setCampaign(null);
            gameCharacterRepository.save(gameCharacter);
        }
        if (redirectTo.equals(GAME_CHARACTER_ATTR)) return GAME_CHARACTER_PATH + "/" + id + DETAILS_PATH;
        return campaignId != null ? "redirect:/campaign/" + campaignId + DETAILS_PATH : GAME_CHARACTER_PATH;
    }
    
    @PostMapping("/assignCampaign")
    public String assignCampaign(
        @RequestParam UUID gameCharacterId,
        @RequestParam UUID campaignId,
        @RequestParam(defaultValue = "campaign") String redirectTo
    ) {
        GameCharacter gameCharacter = gameCharacterRepository.findById(gameCharacterId).orElseThrow();
        Campaign campaign = campaignRepository.findById(campaignId).orElseThrow();
        gameCharacter.setCampaign(campaign);
        gameCharacterRepository.save(gameCharacter);
        if (redirectTo.equals(GAME_CHARACTER_ATTR)) return GAME_CHARACTER_PATH + "/" + gameCharacterId + DETAILS_PATH;
        return "redirect:/campaign/" + campaignId + DETAILS_PATH;
    }
    
    
}
