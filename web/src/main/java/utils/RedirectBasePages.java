package utils;

import lombok.Getter;

@Getter
public enum RedirectBasePages {
    CLIENT("redirect:/clients/allClients"),
    SERVICE("redirect:/services/allServices"),
    ROOM("redirect:/rooms/allRooms");

    private final String url;

    RedirectBasePages(String url) {
        this.url = url;
    }
}
