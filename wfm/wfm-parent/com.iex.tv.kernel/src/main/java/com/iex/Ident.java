/**
 * @copyright 2006 IEX, A Tekelec Company
 * @author gulledge
 * @version "$Id: Ident.java 68667 2009-11-30 20:14:39Z cgulledge $"
 */

package com.iex;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 *  Annotation for all TotalView source files.
 *  Will place class-level string identifier in generated class files
 *
 * Note: this is a copy from the TotalView commons project.
 * 
 * @author gulledge
 * @since Mar 24, 2006
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.CLASS)
public @interface Ident
{
	String[] value();
}
