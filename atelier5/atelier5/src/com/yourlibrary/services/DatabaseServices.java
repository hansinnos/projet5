package com.yourlibrary.services;

import java.sql.*;
import java.util.HashMap;
import com.yourlibrary.models.Livre;
import com.yourlibrary.models.Roman;
import com.yourlibrary.models.ScienceFiction;
import com.yourlibrary.models.Biographie;


import com.yourlibrary.models.*;

import java.sql.*;
import java.util.HashMap;

        public class DatabaseServices {
            private Connection connection;

            public DatabaseServices() {
                try {
                    // Charger le pilote JDBC
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    // Établir une connexion
                    String url = "jdbc:mysql://sql.freedb.tech/freedb_bibliotheque";
                    String user = "freedb_hansinnos";
                    String password = "Rx@XRNjDpu4pb3g";
                    connection = DriverManager.getConnection(url, user, password);
                } catch (ClassNotFoundException e) {
                    System.err.println("Pilote JDBC non trouvé.");
                    e.printStackTrace();
                } catch (SQLException e) {
                    System.err.println("Erreur de connexion à la base de données.");
                    e.printStackTrace();
                }
            }

            public HashMap<Integer, Livre> chargerLivres() {
                HashMap<Integer, Livre> livres = new HashMap<>();
                try (Statement statement = connection.createStatement()) {
                    ResultSet resultSet = statement.executeQuery("SELECT * FROM livres");
                    while (resultSet.next()) {
                        int id = resultSet.getInt("id");
                        String nom = resultSet.getString("nom");
                        String auteur = resultSet.getString("auteur");
                        String categorie = resultSet.getString("categorie");

                        Livre livre;
                        switch (categorie.toLowerCase()) {
                            case "roman":
                                livre = new Roman(id, nom, auteur);
                                break;
                            case "science-fiction":
                                livre = new ScienceFiction(id, nom, auteur);
                                break;
                            case "biographie":
                                livre = new Biographie(id, nom, auteur);
                                break;
                            default:
                                livre = new Livre(id, nom, auteur); // Ou gérez d'une autre manière
                                break;
                        }
                        livres.put(id, livre);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return livres;
            }

            public void ajouterLivre(Livre livre) {
                String categorie = livre.getClass().getSimpleName().toLowerCase();
                String sql = "INSERT INTO livres (id, nom, auteur, categorie) VALUES (?, ?, ?, ?)";
                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                    preparedStatement.setInt(1, livre.getId());
                    preparedStatement.setString(2, livre.getNom());
                    preparedStatement.setString(3, livre.getAuteur());
                    preparedStatement.setString(4, categorie);
                    preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            public void sauvegarderBibliotheque(HashMap<Integer, Livre> livres) {
                try {
                    for (Livre livre : livres.values()) {
                        PreparedStatement ps = connection.prepareStatement(
                                "INSERT INTO livres (id, nom, auteur, categorie) VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE nom = ?, auteur = ?, type = ?"
                        );
                        ps.setInt(1, livre.getId());
                        ps.setString(2, livre.getNom());
                        ps.setString(3, livre.getAuteur());
                        ps.setString(4, livre.getClass().getSimpleName().toLowerCase());
                        ps.setString(5, livre.getNom());
                        ps.setString(6, livre.getAuteur());
                        ps.setString(7, livre.getClass().getSimpleName().toLowerCase());
                        ps.executeUpdate();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } chargerLivres();
            }
            // Méthode pour fermer la connexion
            public void close() {
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }


