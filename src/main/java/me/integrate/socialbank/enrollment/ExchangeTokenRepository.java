package me.integrate.socialbank.enrollment;

interface ExchangeTokenRepository {
    String getExchangeToken(int eventId, String username);

    void save(int eventId, String username, String exchangeToken);
}
