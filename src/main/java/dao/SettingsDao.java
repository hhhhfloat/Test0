package dao;

import model.state.Settings;

public interface SettingsDao {
    void saveSettings(Settings settings);
    Settings loadSettings();
}
