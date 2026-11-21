package dev.jfgonzalez.campaigndb.controller;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import dev.jfgonzalez.campaigndb.dto.CampaignDTO;
import dev.jfgonzalez.campaigndb.model.Campaign;
import dev.jfgonzalez.campaigndb.model.Feature;
import dev.jfgonzalez.campaigndb.model.GameCharacter;
import dev.jfgonzalez.campaigndb.model.Player;
import dev.jfgonzalez.campaigndb.model.Resource;
import dev.jfgonzalez.campaigndb.model.WikiArticle;
import dev.jfgonzalez.campaigndb.model.Item;
import dev.jfgonzalez.campaigndb.repository.CampaignRepository;
import dev.jfgonzalez.campaigndb.repository.FeatureRepository;
import dev.jfgonzalez.campaigndb.repository.GameCharacterRepository;
import dev.jfgonzalez.campaigndb.repository.ItemRepository;
import dev.jfgonzalez.campaigndb.repository.PlayerRepository;
import dev.jfgonzalez.campaigndb.repository.ResourceRepository;
import dev.jfgonzalez.campaigndb.repository.WikiArticleRepository;





@Controller
@RequestMapping("/campaign")
public class CampaignController {
    
    @SuppressWarnings("java:S1075")
    private static final String CAMPAIGN_PATH = "redirect:/campaign";
    static final String CAMPAIGN_ATTR = "campaign";
    
    private final CampaignRepository campaignRepository;
    private final PlayerRepository playerRepository;
    private final GameCharacterRepository gameCharacterRepository;
    private final ResourceRepository resourceRepository;
    private final FeatureRepository featureRepository;
    private final ItemRepository itemRepository;
    private final WikiArticleRepository wikiArticleRepository;

    public CampaignController(
        CampaignRepository campaignRepository,
        PlayerRepository playerRepository,
        GameCharacterRepository gameCharacterRepository,
        ResourceRepository resourceRepository,
        FeatureRepository featureRepository,
        ItemRepository itemRepository,
        WikiArticleRepository wikiArticleRepository
    ) {
         this.campaignRepository = campaignRepository;
         this.playerRepository = playerRepository;
         this.gameCharacterRepository = gameCharacterRepository;
         this.resourceRepository = resourceRepository;
         this.featureRepository = featureRepository;
         this.itemRepository = itemRepository;
         this.wikiArticleRepository = wikiArticleRepository;
    }

    @GetMapping
    public String listCampaigns(
        @RequestParam(defaultValue = "0") int pageNumber,
        @RequestParam(defaultValue = "10") int pageSize,
        Model model
    ) {
        Page<Campaign> campaigns = campaignRepository.findAll(PageRequest.of(pageNumber, pageSize));
        if (pageNumber > 0 && campaigns.isEmpty() && campaigns.getTotalPages() > 0) {
            return "redirect:/campaign?pageNumber=" + (campaigns.getTotalPages() - 1) + "&pageSize=" + pageSize;
        }
        model.addAttribute("campaigns", campaigns);
        return "campaign/list";
    }

    @GetMapping("/new")
    public String newCampaign(Model model) {
        CampaignDTO dto = new CampaignDTO(null,"","","","WIP",0,null);
        model.addAttribute(CAMPAIGN_ATTR, dto);
        List<Player> players = playerRepository.findAll();
        model.addAttribute("players", players);
        return "campaign/new";
    }

    @PostMapping("/create")
    public String createCampaign(@ModelAttribute CampaignDTO dto) {
        Campaign campaign = new Campaign();
        campaign.setName(dto.name());
        campaign.setDescription(dto.description());
        campaign.setGameSystem(dto.gameSystem());
        campaign.setStatus(dto.status());
        campaign.setSecondsPlayed(0);
        campaign.setOwner(playerRepository.findById(dto.ownerId()).orElseThrow());
        campaign.setProperties(new HashMap<>());
        campaignRepository.save(campaign);
        return CAMPAIGN_PATH;
    }

    @GetMapping("/{id}/edit")
    public String editCampaign(@PathVariable UUID id, Model model) {
        Campaign campaign = campaignRepository.findById(id).orElseThrow();
        CampaignDTO dto = new CampaignDTO(
            campaign.getId(),
            campaign.getName(),
            campaign.getDescription(),
            campaign.getGameSystem(),
            campaign.getStatus(),
            campaign.getSecondsPlayed(),
            campaign.getOwner().getId()
        );
        model.addAttribute(CAMPAIGN_ATTR, dto);
        List<Player> players = playerRepository.findAll();
        model.addAttribute("players", players);
        return "campaign/edit";
    }

    @PostMapping("/modify")
    public String modifyCampaign(@ModelAttribute CampaignDTO dto) {
        Campaign campaign = campaignRepository.findById(dto.id()).orElseThrow();
        campaign.setName(dto.name());
        campaign.setDescription(dto.description());
        campaign.setGameSystem(dto.gameSystem());
        campaign.setStatus(dto.status());
        campaign.setSecondsPlayed(dto.secondsPlayed());
        campaign.setOwner(playerRepository.findById(dto.ownerId()).orElseThrow());
        campaignRepository.save(campaign);
        return "redirect:/campaign/" + dto.id() + "/details";
    }
    
    @GetMapping("/{id}/delete")
    @Transactional 
    public String deleteCampaign(
        @PathVariable UUID id,
        @RequestParam(defaultValue = "0") int pageNumber,
        @RequestParam(defaultValue = "10") int pageSize,
        RedirectAttributes redirectAttributes
    ) {
        List<GameCharacter> gameCharacters = gameCharacterRepository.findByCampaignId(id);
        gameCharacters.forEach(gc -> gc.setCampaign(null));
        List<Resource> resources = resourceRepository.findByCampaignId(id);
        resources.forEach(r -> r.setCampaign(null));
        List<Feature> features = featureRepository.findByCampaignId(id);
        features.forEach(f -> f.setCampaign(null));
        List<Item> items = itemRepository.findByCampaignId(id);
        items.forEach(i -> i.setCampaign(null));
        List<WikiArticle> wikiArticles = wikiArticleRepository.findByCampaignId(id);
        wikiArticles.forEach(wa -> wa.setCampaign(null));
        campaignRepository.deleteById(id);
        redirectAttributes.addAttribute("pageNumber", pageNumber);
        redirectAttributes.addAttribute("pageSize", pageSize);
        return CAMPAIGN_PATH;
    }

    @GetMapping("/{id}/details")
    public String detailsCampaign(@PathVariable UUID id, Model model) {
        Campaign campaign = campaignRepository.findById(id).orElseThrow();
        model.addAttribute(CAMPAIGN_ATTR, campaign);
        List<GameCharacter> gameCharacters = gameCharacterRepository.findByCampaignId(id);
        model.addAttribute("gameCharacters", gameCharacters);
        List<GameCharacter> gameCharactersWithoutCampaign = gameCharacterRepository.findByCampaignIsNull();
        model.addAttribute("gameCharacterWithoutCampaign", gameCharactersWithoutCampaign);
        return "campaign/details";
    }    
}
