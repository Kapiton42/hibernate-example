package ru.hh.hibernateexample;

import org.hibernate.cfg.Configuration;
import ru.hh.hibernateexample.users.User;

class HibernateConfig {

  public static Configuration prod() {
    return new Configuration().addAnnotatedClass(User.class);
  }

  private HibernateConfig() {
  }
}
