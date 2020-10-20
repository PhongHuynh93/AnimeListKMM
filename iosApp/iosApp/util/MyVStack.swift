//
//  MyVStack.swift
//  iosApp
//
//  Created by Coder on 10/18/20.
//  Copyright © 2020 orgName. All rights reserved.
//

import Foundation
import SwiftUI

struct MyVStack<Content>: View where Content : View {

  let content: () -> Content
  let alignment: HorizontalAlignment
  let spacing: CGFloat?

  public init(alignment: HorizontalAlignment = .center, spacing: CGFloat? = nil, @ViewBuilder content: @escaping () -> Content) {
    self.content = content
    self.alignment = alignment
    self.spacing = spacing
  }

  @ViewBuilder public var body: some View {
      if #available(iOS 14.0, *) {
          LazyVStack(alignment: alignment, spacing: spacing, content: self.content)
      } else {
          VStack(alignment: alignment, spacing: spacing, content: self.content)
      }
  }
}
