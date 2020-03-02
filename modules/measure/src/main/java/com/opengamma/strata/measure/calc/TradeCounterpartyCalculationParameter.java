/*
 * Copyright (C) 2016 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.strata.measure.calc;

import java.io.Serializable;
import java.lang.invoke.MethodHandles;
import java.util.Map;
import java.util.Optional;

import org.joda.beans.ImmutableBean;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaBean;
import org.joda.beans.TypedMetaBean;
import org.joda.beans.gen.BeanDefinition;
import org.joda.beans.gen.PropertyDefinition;
import org.joda.beans.impl.light.LightMetaBean;

import com.google.common.collect.ImmutableMap;
import com.opengamma.strata.basics.CalculationTarget;
import com.opengamma.strata.basics.StandardId;
import com.opengamma.strata.calc.Measure;
import com.opengamma.strata.calc.runner.CalculationParameter;
import com.opengamma.strata.collect.ArgChecker;
import com.opengamma.strata.collect.Messages;
import com.opengamma.strata.product.Trade;

/**
 * A calculation parameter that selects the parameter based on the counterparty of the target.
 * <p>
 * This can be used where a {@link CalculationParameter} is required, and will
 * select an underlying parameter based on the trade counterparty represented by {@link StandardId}.
 */
@BeanDefinition(style = "light")
public final class TradeCounterpartyCalculationParameter
    implements CalculationParameter, ImmutableBean, Serializable {

  /**
   * The parameter query type.
   */
  @PropertyDefinition(validate = "notNull")
  private final Class<? extends CalculationParameter> queryType;
  /**
   * The underlying parameters, keyed by counterparty ID.
   */
  @PropertyDefinition(validate = "notNull")
  private final ImmutableMap<StandardId, CalculationParameter> parameters;
  /**
   * The default underlying parameter.
   */
  @PropertyDefinition(validate = "notNull")
  private final CalculationParameter defaultParameter;

  //-------------------------------------------------------------------------
  /**
   * Obtains an instance from the specified parameters.
   * <p>
   * The map provides a lookup from the {@link CalculationTarget} implementation type
   * to the appropriate parameter to use for that target. If a target is requested that
   * is not in the map, the default parameter is used.
   * 
   * @param parameters  the parameters, keyed by target type
   * @param defaultParameter  the default parameter
   * @return the target aware parameter
   */
  public static TradeCounterpartyCalculationParameter of(
      Map<StandardId, CalculationParameter> parameters,
      CalculationParameter defaultParameter) {

    ArgChecker.notEmpty(parameters, "values");
    ArgChecker.notNull(defaultParameter, "defaultParameter");
    Class<? extends CalculationParameter> queryType = defaultParameter.queryType();
    for (CalculationParameter value : parameters.values()) {
      if (value.queryType() != queryType) {
        throw new IllegalArgumentException(Messages.format(
            "Map contained a parameter '{}' that did not match the expected query type '{}'",
            value,
            queryType.getClass().getSimpleName()));
      }
    }
    return new TradeCounterpartyCalculationParameter(queryType, ImmutableMap.copyOf(parameters), defaultParameter);
  }

  //-------------------------------------------------------------------------
  @Override
  public Class<? extends CalculationParameter> queryType() {
    return queryType;
  }

  @Override
  public Optional<CalculationParameter> filter(CalculationTarget target, Measure measure) {
    if (target instanceof Trade) {
      Trade trade = (Trade) target;
      Optional<StandardId> idOpt = trade.getInfo().getCounterparty();
      if (idOpt.isPresent()) {
        StandardId id = idOpt.get();
        CalculationParameter value = parameters.getOrDefault(id, defaultParameter);
        return value.filter(target, measure);
      }
    }
    return defaultParameter.filter(target, measure);
  }

  //------------------------- AUTOGENERATED START -------------------------
  /**
   * The meta-bean for {@code TradeCounterpartyCalculationParameter}.
   */
  private static final TypedMetaBean<TradeCounterpartyCalculationParameter> META_BEAN =
      LightMetaBean.of(
          TradeCounterpartyCalculationParameter.class,
          MethodHandles.lookup(),
          new String[] {
              "queryType",
              "parameters",
              "defaultParameter"},
          null,
          ImmutableMap.of(),
          null);

  /**
   * The meta-bean for {@code TradeCounterpartyCalculationParameter}.
   * @return the meta-bean, not null
   */
  public static TypedMetaBean<TradeCounterpartyCalculationParameter> meta() {
    return META_BEAN;
  }

  static {
    MetaBean.register(META_BEAN);
  }

  /**
   * The serialization version id.
   */
  private static final long serialVersionUID = 1L;

  private TradeCounterpartyCalculationParameter(
      Class<? extends CalculationParameter> queryType,
      Map<StandardId, CalculationParameter> parameters,
      CalculationParameter defaultParameter) {
    JodaBeanUtils.notNull(queryType, "queryType");
    JodaBeanUtils.notNull(parameters, "parameters");
    JodaBeanUtils.notNull(defaultParameter, "defaultParameter");
    this.queryType = queryType;
    this.parameters = ImmutableMap.copyOf(parameters);
    this.defaultParameter = defaultParameter;
  }

  @Override
  public TypedMetaBean<TradeCounterpartyCalculationParameter> metaBean() {
    return META_BEAN;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the parameter query type.
   * @return the value of the property, not null
   */
  public Class<? extends CalculationParameter> getQueryType() {
    return queryType;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the underlying parameters, keyed by counterparty ID.
   * @return the value of the property, not null
   */
  public ImmutableMap<StandardId, CalculationParameter> getParameters() {
    return parameters;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the default underlying parameter.
   * @return the value of the property, not null
   */
  public CalculationParameter getDefaultParameter() {
    return defaultParameter;
  }

  //-----------------------------------------------------------------------
  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      TradeCounterpartyCalculationParameter other = (TradeCounterpartyCalculationParameter) obj;
      return JodaBeanUtils.equal(queryType, other.queryType) &&
          JodaBeanUtils.equal(parameters, other.parameters) &&
          JodaBeanUtils.equal(defaultParameter, other.defaultParameter);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash = hash * 31 + JodaBeanUtils.hashCode(queryType);
    hash = hash * 31 + JodaBeanUtils.hashCode(parameters);
    hash = hash * 31 + JodaBeanUtils.hashCode(defaultParameter);
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(128);
    buf.append("TradeCounterpartyCalculationParameter{");
    buf.append("queryType").append('=').append(JodaBeanUtils.toString(queryType)).append(',').append(' ');
    buf.append("parameters").append('=').append(JodaBeanUtils.toString(parameters)).append(',').append(' ');
    buf.append("defaultParameter").append('=').append(JodaBeanUtils.toString(defaultParameter));
    buf.append('}');
    return buf.toString();
  }

  //-------------------------- AUTOGENERATED END --------------------------
}
